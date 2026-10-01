// Собирает отчёты .docx из tools/reports/*.md.
// Поддерживается упрощённый Markdown:
//   # / ## / ###       — заголовки
//   - пункт, 1. пункт  — списки
//   | a | b |          — таблицы (вторая строка |---|)
//   ```lang ... ```    — блок кода
//   > текст            — примечание
//   **жирный**, *курсив*, `код`
// Директивы:
//   <!-- target: путь/к/Отчёт.docx -->   — куда сохранить (от корня репозитория)
//   @code путь [N-M]                     — вставить файл (путь от папки отчёта), опционально строки N..M
//   @out имя                             — вставить вывод программы из tools/out/имя.txt
//   @img файл.png | подпись              — рисунок из tools/img (Logcat), с подписью «Рисунок N — …»
//   @imgrow N[:см] | файл1 | подпись1 | … — снимки экрана в ряд по N штук (ширина каждого не больше см)
const fs = require("fs");
const path = require("path");
const {
  Document, Packer, Paragraph, TextRun, AlignmentType, LevelFormat, Table, TableRow,
  TableCell, WidthType, BorderStyle, ShadingType, Footer, PageNumber, HeadingLevel, ImageRun,
} = require("docx");

const ROOT = path.resolve(__dirname, "..");
const FONT = "Times New Roman";
const MONO = "Courier New";
const BODY = 28;        // 14 pt
const CODE = 16;        // 8 pt
const TEXT_WIDTH = 9354; // A4 минус поля 3 см и 1,5 см, в DXA

const codeBorder = { style: BorderStyle.SINGLE, size: 4, color: "BFBFBF", space: 4 };
const outBorder = { style: BorderStyle.SINGLE, size: 4, color: "A9C4A0", space: 4 };

function inlineRuns(text, base = {}) {
  const runs = [];
  const re = /(\*\*.+?\*\*|`[^`]+`|\*[^*\s][^*]*\*)/g;
  let last = 0, m;
  while ((m = re.exec(text))) {
    if (m.index > last) runs.push(new TextRun({ text: text.slice(last, m.index), ...base }));
    const t = m[0];
    if (t.startsWith("**")) runs.push(...inlineRuns(t.slice(2, -2), { ...base, bold: true }));
    else if (t.startsWith("`")) runs.push(new TextRun({ text: t.slice(1, -1), font: MONO, size: (base.size || BODY) - 4,
      bold: base.bold, italics: base.italics }));
    else runs.push(new TextRun({ text: t.slice(1, -1), italics: true, ...base }));
    last = m.index + t.length;
  }
  if (last < text.length) runs.push(new TextRun({ text: text.slice(last), ...base }));
  return runs;
}

function codeBlock(lines, kind = "code") {
  const border = kind === "out" ? outBorder : codeBorder;
  const fill = kind === "out" ? "F1F7EE" : "F4F4F4";
  while (lines.length && lines[lines.length - 1].trim() === "") lines.pop();
  return lines.map((line, i) => new Paragraph({
    keepNext: i < lines.length - 1 && lines.length <= 14,
    spacing: { before: i === 0 ? 60 : 0, after: i === lines.length - 1 ? 160 : 0, line: 240 },
    indent: { left: 113, right: 113 },
    shading: { type: ShadingType.CLEAR, fill, color: "auto" },
    border: { top: border, left: border, bottom: border, right: border },
    children: [new TextRun({ text: line.replace(/\t/g, "    ") || " ", font: MONO, size: CODE })],
  }));
}

// Вырезает из Java-файла объявления методов/конструкторов с заданными именами
// (все перегрузки) вместе с комментариями и аннотациями над ними.
function extractMembers(lines, names, file) {
  const parts = [];
  lines.forEach((line, i) => {
    const decl = names.some((n) => new RegExp(`^\\s*(?:[\\w<>\\[\\],]+\\s+)*${n}\\s*\\([^;]*$`).test(line)
      && !/[=.]\s*\w+\s*\(/.test(line.split("(")[0]) && !/\b(new|return)\b/.test(line.split("(")[0]));
    if (!decl) return;
    let start = i;
    while (start > 0 && /^\s*(\/\/|@\w+)/.test(lines[start - 1])) start--;
    let depth = 0, end = i, opened = false;
    for (let j = i; j < lines.length; j++) {
      for (const ch of lines[j].replace(/"(?:\\.|[^"\\])*"/g, "").replace(/\/\/.*$/, "")) {
        if (ch === "{") { depth++; opened = true; }
        if (ch === "}") depth--;
      }
      if (opened && depth === 0) { end = j; break; }
    }
    parts.push(lines.slice(start, end + 1));
  });
  if (!parts.length) throw new Error(`members ${names} not found in ${file}`);
  const all = parts.flatMap((p, k) => (k ? [""] : []).concat(p));
  const indent = Math.min(...all.filter((l) => l.trim()).map((l) => l.match(/^ */)[0].length));
  return all.map((l) => l.slice(indent));
}

// Фрагмент от первой строки, содержащей from, до первой следующей строки, содержащей to
function extractRange(lines, from, to, file) {
  const s = lines.findIndex((l) => l.includes(from));
  let e = lines.findIndex((l, i) => i >= s && l.includes(to));
  if (s < 0 || e < 0) throw new Error(`range "${from}"…"${to}" not found in ${file}`);
  // дотягиваем до закрывающих скобок, чтобы фрагмент был сбалансирован
  const balance = (a, b) => lines.slice(a, b + 1).join("\n").replace(/"(?:\\.|[^"\\])*"/g, "")
    .replace(/\/\/.*$/gm, "").split("").reduce((d, ch) => d + (ch === "{") - (ch === "}"), 0);
  while (balance(s, e) > 0 && e < lines.length - 1) e++;
  const part = lines.slice(s, e + 1);
  const indent = Math.min(...part.filter((l) => l.trim()).map((l) => l.match(/^ */)[0].length));
  return part.map((l) => l.slice(indent));
}

// ---------- Рисунки ----------
const IMG_DIR = path.join(__dirname, "img");
const CM = 96 / 2.54;   // docx-js принимает размеры картинок в пикселях при 96 dpi

function pngSize(file) {
  const b = fs.readFileSync(file);
  return { w: b.readUInt32BE(16), h: b.readUInt32BE(20), data: b };
}

function imageRun(file, widthCm) {
  const { w, h, data } = pngSize(path.join(IMG_DIR, file));
  const width = widthCm * CM;
  return new ImageRun({ type: "png", data, transformation: { width: Math.round(width), height: Math.round(width * h / w) } });
}

function figCaption(n, text) {
  return new Paragraph({
    alignment: AlignmentType.CENTER,
    spacing: { before: 60, after: 200 },
    children: inlineRuns(`Рисунок ${n} — ${text}`, { size: BODY - 4 }),
  });
}

// Logcat: масштаб одинаковый для всех картинок (чтобы шрифт был одного размера), но не шире страницы
function logcatFigure(file, n, text) {
  const { w } = pngSize(path.join(IMG_DIR, file));
  const widthCm = Math.min(16.2, w * 0.0135);
  return [
    new Paragraph({ alignment: AlignmentType.CENTER, keepNext: true, spacing: { before: 120, line: 240, lineRule: "auto" }, children: [imageRun(file, widthCm)] }),
    figCaption(n, text),
  ];
}

// Ряд снимков экрана: таблица без рамок, под каждым снимком своя подпись
function screenRow(items, perRow, nextNum, maxCm) {
  const colW = Math.floor(TEXT_WIDTH / perRow);
  const none = { style: BorderStyle.NONE, size: 0, color: "FFFFFF" };
  const rows = [];
  for (let i = 0; i < items.length; i += perRow) {
    const chunk = items.slice(i, i + perRow);
    while (chunk.length < perRow) chunk.push(null);
    rows.push(new TableRow({
      cantSplit: true,
      children: chunk.map((it) => new TableCell({
        width: { size: colW, type: WidthType.DXA },
        borders: { top: none, left: none, bottom: none, right: none },
        margins: { left: 60, right: 60 },
        children: it ? [
          new Paragraph({ alignment: AlignmentType.CENTER, spacing: { line: 240, lineRule: "auto" }, children: [imageRun(it.file, Math.min(colW / 567 - 0.4, maxCm || 99))] }),
          new Paragraph({ alignment: AlignmentType.CENTER, spacing: { before: 60, after: 160, line: 240 },
            children: inlineRuns(`Рисунок ${nextNum()} — ${it.caption}`, { size: BODY - 6 }) }),
        ] : [new Paragraph({ children: [] })],
      })),
    }));
  }
  return new Table({ width: { size: colW * perRow, type: WidthType.DXA }, columnWidths: Array(perRow).fill(colW), rows });
}

function caption(text) {
  return new Paragraph({
    keepNext: true,
    spacing: { before: 120, after: 40 },
    children: [new TextRun({ text, italics: true, size: BODY - 4 })],
  });
}

function table(rows) {
  const cols = rows[0].length;
  const widths = Array(cols).fill(Math.floor(TEXT_WIDTH / cols));
  widths[cols - 1] = TEXT_WIDTH - widths.slice(0, -1).reduce((a, b) => a + b, 0);
  const cellBorder = { style: BorderStyle.SINGLE, size: 4, color: "808080" };
  return new Table({
    width: { size: TEXT_WIDTH, type: WidthType.DXA },
    columnWidths: widths,
    rows: rows.map((r, ri) => new TableRow({
      tableHeader: ri === 0,
      children: r.map((c, ci) => new TableCell({
        width: { size: widths[ci], type: WidthType.DXA },
        shading: ri === 0 ? { type: ShadingType.CLEAR, fill: "E7E6E6", color: "auto" } : undefined,
        borders: { top: cellBorder, left: cellBorder, bottom: cellBorder, right: cellBorder },
        margins: { top: 40, bottom: 40, left: 80, right: 80 },
        children: [new Paragraph({
          spacing: { line: 240 },
          children: inlineRuns(c, ri === 0 ? { bold: true, size: BODY - 4 } : { size: BODY - 4 }),
        })],
      })),
    })),
  });
}

function build(mdFile) {
  const src = fs.readFileSync(mdFile, "utf8");
  const target = (src.match(/<!--\s*target:\s*(.+?)\s*-->/) || [])[1];
  if (!target) throw new Error("no target in " + mdFile);
  const reportDir = path.dirname(path.join(ROOT, target));
  const lines = src.split("\n");
  const out = [];
  let para = [];
  let listCounter = 0;
  let figNum = 0;
  let prevList = null;

  const flush = () => {
    if (para.length) {
      out.push(new Paragraph({
        alignment: AlignmentType.JUSTIFIED,
        indent: { firstLine: 709 },
        spacing: { after: 80 },
        children: inlineRuns(para.join(" ")),
      }));
      para = [];
    }
  };

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    const t = line.trim();
    const listType = /^- /.test(t) ? "bullet" : /^\d+\. /.test(t) ? "number" : null;
    if (listType && listType !== prevList) listCounter++;
    if (!listType && t !== "") prevList = null;

    if (t.startsWith("<!--")) continue;
    if (t.startsWith("```")) {
      flush();
      const block = [];
      for (i++; i < lines.length && !lines[i].trim().startsWith("```"); i++) block.push(lines[i]);
      out.push(...codeBlock(block));
      continue;
    }
    if (t.startsWith("@code ")) {
      flush();
      const m = t.match(/^@code\s+([^#~]+?)\s*(?:#(.+)|~(.+)~(.+))?$/);
      const file = path.join(reportDir, m[1]);
      let code = fs.readFileSync(file, "utf8").split("\n");
      if (m[2]) code = extractMembers(code, m[2].split(",").map((s) => s.trim()), m[1]);
      if (m[3]) code = extractRange(code, m[3].trim(), m[4].trim(), m[1]);
      out.push(...codeBlock(code));
      continue;
    }
    if (t.startsWith("@out ")) {
      flush();
      const name = t.slice(5).trim();
      const text = fs.readFileSync(path.join(__dirname, "out", name + ".txt"), "utf8");
      out.push(...codeBlock(text.split("\n"), "out"));
      continue;
    }
    if (t === "") { flush(); continue; }

    const h = t.match(/^(#{1,3}) (.*)$/);
    if (h) {
      flush();
      const level = h[1].length;
      out.push(new Paragraph({
        heading: [HeadingLevel.HEADING_1, HeadingLevel.HEADING_2, HeadingLevel.HEADING_3][level - 1],
        alignment: level === 1 ? AlignmentType.CENTER : AlignmentType.LEFT,
        keepNext: true,
        children: inlineRuns(h[2]),
      }));
      continue;
    }
    if (t.startsWith("|")) {
      flush();
      const rows = [];
      for (; i < lines.length && lines[i].trim().startsWith("|"); i++) {
        const cells = lines[i].trim().replace(/^\||\|$/g, "").split("|").map((c) => c.trim());
        if (cells.every((c) => /^:?-+:?$/.test(c))) continue;
        rows.push(cells);
      }
      i--;
      out.push(table(rows));
      out.push(new Paragraph({ spacing: { after: 80 }, children: [] }));
      continue;
    }
    if (t.startsWith("> ")) {
      flush();
      out.push(new Paragraph({
        alignment: AlignmentType.JUSTIFIED,
        indent: { left: 340 },
        spacing: { before: 60, after: 120 },
        border: { left: { style: BorderStyle.SINGLE, size: 12, color: "7F9DB9", space: 8 } },
        children: inlineRuns(t.slice(2), { italics: true, size: BODY - 2 }),
      }));
      continue;
    }
    if (t.startsWith("@img ")) {
      flush();
      const [file, text] = t.slice(5).split("|").map((x) => x.trim());
      out.push(...logcatFigure(file, ++figNum, text));
      continue;
    }
    if (t.startsWith("@imgrow ")) {
      flush();
      const parts = t.slice(8).split("|").map((x) => x.trim());
      const [perRow, maxCm] = parts[0].split(":").map(Number);   // «2:6» — 2 в ряд, не шире 6 см
      const items = [];
      for (let k = 1; k < parts.length; k += 2) items.push({ file: parts[k], caption: parts[k + 1] });
      out.push(screenRow(items, perRow, () => ++figNum, maxCm));
      out.push(new Paragraph({ spacing: { after: 80 }, children: [] }));
      continue;
    }
    if (t.startsWith("@caption ")) {
      flush();
      out.push(caption(t.slice(9)));
      continue;
    }
    if (listType) {
      flush();
      prevList = listType;
      const text = t.replace(/^(- |\d+\. )/, "");
      out.push(new Paragraph({
        alignment: AlignmentType.JUSTIFIED,
        numbering: { reference: listType, level: 0, instance: listCounter },
        spacing: { after: 40 },
        children: inlineRuns(text),
      }));
      continue;
    }
    para.push(t);
  }
  flush();

  const doc = new Document({
    styles: {
      default: { document: { run: { font: FONT, size: BODY }, paragraph: { spacing: { line: 276, lineRule: "auto" } } } },
      paragraphStyles: [
        { id: "Heading1", name: "Heading 1", basedOn: "Normal", next: "Normal", quickFormat: true,
          run: { size: 32, bold: true, font: FONT, color: "000000" },
          paragraph: { spacing: { before: 0, after: 240 }, outlineLevel: 0 } },
        { id: "Heading2", name: "Heading 2", basedOn: "Normal", next: "Normal", quickFormat: true,
          run: { size: 28, bold: true, font: FONT, color: "000000" },
          paragraph: { spacing: { before: 280, after: 120 }, outlineLevel: 1 } },
        { id: "Heading3", name: "Heading 3", basedOn: "Normal", next: "Normal", quickFormat: true,
          run: { size: 28, bold: true, italics: true, font: FONT, color: "000000" },
          paragraph: { spacing: { before: 200, after: 80 }, outlineLevel: 2 } },
      ],
    },
    numbering: {
      config: [
        { reference: "bullet", levels: [{ level: 0, format: LevelFormat.BULLET, text: "–", alignment: AlignmentType.LEFT,
          style: { paragraph: { indent: { left: 709, hanging: 284 } } } }] },
        { reference: "number", levels: [{ level: 0, format: LevelFormat.DECIMAL, text: "%1.", alignment: AlignmentType.LEFT,
          style: { paragraph: { indent: { left: 709, hanging: 360 } } } }] },
      ],
    },
    sections: [{
      properties: {
        page: {
          size: { width: 11906, height: 16838 },
          margin: { top: 1134, bottom: 1134, left: 1701, right: 851 },
        },
      },
      footers: {
        default: new Footer({
          children: [new Paragraph({
            alignment: AlignmentType.CENTER,
            children: [new TextRun({ children: [PageNumber.CURRENT], size: 24 })],
          })],
        }),
      },
      children: out,
    }],
  });

  const dest = path.join(ROOT, target);
  fs.mkdirSync(path.dirname(dest), { recursive: true });
  return Packer.toBuffer(doc).then(fixBorderOrder).then((buf) => {
    fs.writeFileSync(dest, buf);
    console.log("✓ " + target);
  });
}

// docx-js пишет границы в порядке top, bottom, left, right, а схема OOXML
// требует top, left, bottom, right — переставляем, чтобы Word не ругался.
async function fixBorderOrder(buf) {
  const JSZip = require(require.resolve("jszip", { paths: [path.dirname(require.resolve("docx"))] }));
  const zip = await JSZip.loadAsync(buf);
  let xml = await zip.file("word/document.xml").async("string");
  const order = ["top", "left", "start", "bottom", "right", "end", "insideH", "insideV"];
  xml = xml.replace(/<w:(pBdr|tcBorders)>(.*?)<\/w:\1>/g, (all, tag, inner) => {
    const items = inner.match(/<w:(\w+)\b[^>]*\/>/g) || [];
    items.sort((a, b) => order.indexOf(a.match(/<w:(\w+)/)[1]) - order.indexOf(b.match(/<w:(\w+)/)[1]));
    return `<w:${tag}>${items.join("")}</w:${tag}>`;
  });
  zip.file("word/document.xml", xml);
  return zip.generateAsync({ type: "nodebuffer", compression: "DEFLATE" });
}

const dir = path.join(__dirname, "reports");
const only = process.argv.slice(2);
const files = fs.readdirSync(dir).filter((f) => f.endsWith(".md"))
  .filter((f) => !only.length || only.some((o) => f.includes(o))).sort();
(async () => {
  for (const f of files) await build(path.join(dir, f));
})().catch((e) => { console.error(e); process.exit(1); });
