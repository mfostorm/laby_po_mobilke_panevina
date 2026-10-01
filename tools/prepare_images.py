"""Готовит картинки для отчётов из папки screenshots/ (её создаёт задача android → screenshots):
- *__logcat.txt  → tools/img/<app>__logcat.png: строки Logcat самого приложения (по его тегам),
  отрисованные моноширинным шрифтом в цветах Logcat;
- снимки экрана  → tools/img/<app>__<шаг>.png: копия, при необходимости обрезанная снизу."""
import glob
import os
import re

from PIL import Image, ImageDraw, ImageFont

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SRC = os.path.join(ROOT, "screenshots")
OUT = os.path.join(ROOT, "tools", "img")
os.makedirs(OUT, exist_ok=True)

# Теги, которые пишут сами приложения (всё остальное в логе — системные сообщения)
APP_TAGS = {"Info:", "info", "Car Speed:", "Plane Speed:", "Total Vehicles:", "Salary:",
            "Total Employees:", "First Employee:", "Soldier", "BankAccount", "Student"}
LINE = re.compile(r"^(\S+ \S+) ([VDIWE])/(.+?)\s*\(\s*(\d+)\): (.*)$")

# Высота, до которой обрезаются экраны (пустой низ не нужен), px
CROP = {"widgets": 1500, "widgetexploration": 1500}

FONT = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf", 26)
BG, GRAY, TEXT = (43, 43, 43), (140, 140, 140), (220, 220, 220)
LEVEL = {"I": (106, 171, 115), "W": (215, 186, 125), "E": (230, 110, 110), "D": (104, 151, 187), "V": (180, 180, 180)}


def render_logcat(txt, png):
    rows = []
    for line in open(txt, encoding="utf-8"):
        m = LINE.match(line.rstrip("\n"))
        if m and m.group(3) in APP_TAGS:
            rows.append(m.groups())
    if not rows:
        return 0
    pad, lh, gap = 24, 36, 60
    parts = [[(t, GRAY), (f" {lv}/{tag}", LEVEL[lv]), (f" ({pid}): ", GRAY), (msg, TEXT)]
             for t, lv, tag, pid, msg in rows]
    # Длинный лог (больше 25 строк) раскладываем в колонки, чтобы картинка влезла на страницу
    per_col = -(-len(parts) // -(-len(parts) // 25))
    cols = [parts[i:i + per_col] for i in range(0, len(parts), per_col)]
    col_w = [max(sum(FONT.getlength(s) for s, _ in p) for p in col) for col in cols]
    width = sum(col_w) + gap * (len(cols) - 1) + 2 * pad
    img = Image.new("RGB", (int(width), pad * 2 + lh * per_col - (lh - 30)), BG)
    draw = ImageDraw.Draw(img)
    x0 = pad
    for col, cw in zip(cols, col_w):
        for i, p in enumerate(col):
            x = x0
            for s, color in p:
                draw.text((x, pad + i * lh), s, font=FONT, fill=color)
                x += FONT.getlength(s)
        x0 += cw + gap
    img.save(png, optimize=True)
    return len(rows)


for txt in sorted(glob.glob(os.path.join(SRC, "*__logcat.txt"))):
    app = os.path.basename(txt).split("__")[0]
    n = render_logcat(txt, os.path.join(OUT, f"{app}__logcat.png"))
    print(f"{app}: {n} строк Logcat")

for png in sorted(glob.glob(os.path.join(SRC, "*__*.png"))):
    name = os.path.basename(png)
    app = name.split("__")[0]
    if name.endswith("__screen.png"):
        continue   # экран «Hello World!» у Log-приложений в отчёт не идёт
    img = Image.open(png).convert("RGB")
    if app in CROP:
        img = img.crop((0, 0, img.width, min(CROP[app], img.height)))
    img.save(os.path.join(OUT, name), optimize=True)
    print(f"{name}: {img.width}x{img.height}")
