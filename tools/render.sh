#!/bin/bash
# Рендер DOCX → JPG для визуальной проверки: tools/render.sh <файл.docx> <папка_вывода>
set -e
out="$2"; mkdir -p "$out"
cp "$1" "$out/doc.docx"
cd "$out" && rm -f doc.pdf p-*.jpg
timeout 120 soffice --headless --convert-to pdf doc.docx >/dev/null 2>&1
pdftoppm -jpeg -r 60 doc.pdf p
ls p-*.jpg | wc -l
