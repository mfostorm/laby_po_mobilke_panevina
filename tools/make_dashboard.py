"""Генерирует макет дашборда мониторинга ИТ-инфраструктуры для Draw.io (ЛР4, часть 2, задание 5).
Файл открывается в https://app.diagrams.net (File → Open from → Device), экспорт: File → Export as → PNG."""
import os
from xml.sax.saxutils import escape

OUT = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
                   "другие предметы", "ЛР4 Мониторинг и инциденты", "Дашборд мониторинга.drawio")

GREEN, YELLOW, RED, GREY = "#43A047", "#FBC02D", "#E53935", "#CFD8DC"
cells = []


def box(x, y, w, h, text="", fill="none", stroke="none", font=13, color="#263238", bold=False,
        align="center", rounded=0, extra=""):
    style = (f"rounded={rounded};whiteSpace=wrap;html=1;fillColor={fill};strokeColor={stroke};"
             f"fontSize={font};fontColor={color};align={align};verticalAlign=middle;"
             f"fontStyle={1 if bold else 0};spacingLeft=4;{extra}")
    cells.append(f'<mxCell id="c{len(cells)}" value="{escape(text, {chr(34): "&quot;"})}" style="{style}" '
                 f'vertex="1" parent="1"><mxGeometry x="{x}" y="{y}" width="{w}" height="{h}" as="geometry"/></mxCell>')


def circle(x, y, d, fill):
    cells.append(f'<mxCell id="c{len(cells)}" value="" style="ellipse;whiteSpace=wrap;html=1;fillColor={fill};'
                 f'strokeColor=none;" vertex="1" parent="1"><mxGeometry x="{x}" y="{y}" width="{d}" height="{d}" '
                 f'as="geometry"/></mxCell>')


def line(x1, y1, x2, y2, color="#90A4AE", dashed=False, width=1):
    cells.append(f'<mxCell id="c{len(cells)}" value="" style="endArrow=none;html=1;strokeColor={color};'
                 f'strokeWidth={width};dashed={1 if dashed else 0};" edge="1" parent="1">'
                 f'<mxGeometry relative="1" as="geometry"><mxPoint x="{x1}" y="{y1}" as="sourcePoint"/>'
                 f'<mxPoint x="{x2}" y="{y2}" as="targetPoint"/></mxGeometry></mxCell>')


def panel(x, y, w, h, title):
    box(x, y, w, h, fill="#FFFFFF", stroke="#B0BEC5", rounded=1, extra="arcSize=2;")
    box(x + 10, y + 8, w - 20, 34, title, font=17, bold=True, align="left")
    line(x + 10, y + 46, x + w - 10, y + 46, "#ECEFF1")


def level(v):
    return GREEN if v < 70 else YELLOW if v <= 85 else RED


# Фон и заголовок
box(0, 0, 1600, 940, fill="#ECEFF1")
box(20, 20, 1560, 60, "Дашборд мониторинга ИТ-инфраструктуры  ·  Смена 04.10.2026, 08:00–20:00  ·  "
    "Дежурный: специалист 1-й линии", fill="#263238", font=20, color="#FFFFFF", bold=True, rounded=1,
    extra="arcSize=8;")

# 1. Доступность сервисов
panel(20, 100, 520, 420, "Доступность сервисов")
box(40, 150, 240, 24, "Сервис", font=12, color="#78909C", align="left")
box(280, 150, 130, 24, "Статус", font=12, color="#78909C", align="left")
box(410, 150, 120, 24, "Аптайм · отклик", font=12, color="#78909C", align="left")
services = [
    ("Почтовый сервер", GREEN, "Работает", "99,98 % · 120 мс"),
    ("Веб-сайт компании", RED, "Недоступен (HTTP 500)", "97,10 % · —"),
    ("Система учёта", YELLOW, "Медленно", "99,90 % · 4,2 с"),
    ("Файловый сервер", GREEN, "Работает", "100 % · 15 мс"),
    ("VPN-шлюз", GREEN, "Работает", "99,95 % · 45 мс"),
]
for i, (name, color, status, stat) in enumerate(services):
    y = 185 + i * 64
    box(30, y - 6, 500, 52, fill="#FAFAFA" if i % 2 == 0 else "#FFFFFF")
    circle(42, y + 7, 26, color)
    box(78, y, 200, 40, name, font=15, align="left")
    box(280, y, 130, 40, status, font=13, color=color if color != YELLOW else "#F57F17", bold=True, align="left")
    box(410, y, 120, 40, stat, font=12, color="#546E7A", align="left")

# 2. Производительность
panel(560, 100, 1020, 420, "Производительность (последние 24 часа, условные данные)")
cpu = [12, 10, 9, 8, 8, 9, 15, 28, 45, 62, 74, 92, 81, 55, 60, 79, 70, 52, 40, 30, 22, 18, 15, 13]
ram = [48, 47, 47, 46, 46, 47, 50, 58, 64, 70, 73, 76, 78, 74, 75, 82, 80, 72, 66, 60, 55, 52, 50, 49]
for ox, title, data in [(580, "Загрузка CPU, %", cpu), (1080, "Использование RAM, %", ram)]:
    top, base, scale = 200, 460, 2.5           # 100 % = 250 px
    box(ox + 40, 158, 300, 28, title, font=14, bold=True, align="left")
    box(ox + 330, 158, 140, 28, f"Сейчас: {data[-1]} %", font=13, color=level(data[-1]), bold=True, align="right")
    for pct in (0, 50, 100):
        y = base - pct * scale
        box(ox, y - 10, 36, 20, f"{pct}", font=11, color="#78909C", align="right")
        line(ox + 40, y, ox + 40 + 24 * 19, y, "#ECEFF1")
    for h, v in enumerate(data):
        bh = v * scale
        box(ox + 44 + h * 19, base - bh, 14, bh, fill=level(v))
    threshold = base - 80 * scale
    line(ox + 40, threshold, ox + 40 + 24 * 19, threshold, RED, dashed=True, width=2)
    box(ox + 40 + 24 * 19 - 70, threshold - 22, 70, 18, "порог 80 %", font=10, color=RED, align="right")
    line(ox + 40, base, ox + 40 + 24 * 19, base, "#90A4AE", width=2)
    for h in (0, 6, 12, 18, 23):
        box(ox + 36 + h * 19, base + 4, 30, 18, f"{h}:00" if h else "0:00", font=10, color="#78909C")

# 3. Активные инциденты
panel(20, 540, 1040, 300, "Активные инциденты")
cols = [("№", 110), ("Тема", 330), ("Приоритет", 100), ("Регистрация", 130), ("Ответственный", 190), ("Статус", 130)]
incidents = [
    ("INC-0412", "Веб-сайт компании выдаёт ошибку 500", ("P1", RED), "04.10 09:14", "Иванов А. (2-я линия)", "В работе"),
    ("INC-0413", "Система учёта зависает на 10–15 с", ("P2", YELLOW), "04.10 10:02", "Петрова Е.", "Диагностика"),
    ("INC-0415", "Принтер отдела продаж не в сети", ("P3", GREY), "04.10 13:40", "Сидоров К.", "Назначен"),
]
x0, y0 = 40, 600
x = x0
for name, w in cols:
    box(x, y0, w, 40, name, fill="#CFD8DC", stroke="#B0BEC5", font=13, bold=True)
    x += w
for r, row in enumerate(incidents):
    x, y = x0, y0 + 40 + r * 52
    for c, (name, w) in enumerate(cols):
        val = row[c]
        if isinstance(val, tuple):
            box(x, y, w, 52, fill="#FFFFFF", stroke="#ECEFF1")
            box(x + 25, y + 12, w - 50, 28, val[0], fill=val[1], font=13, bold=True, rounded=1,
                color="#FFFFFF" if val[1] == RED else "#263238")
        else:
            box(x, y, w, 52, val, fill="#FFFFFF", stroke="#ECEFF1", font=13, align="left" if c == 1 else "center")
        x += w
box(40, 800, 1000, 24, "Сортировка: по приоритету, затем по времени регистрации. Срок SLA: P1 — 1 ч, P2 — 4 ч, P3 — 8 ч.",
    font=11, color="#78909C", align="left")

# 4. Сводка за смену
panel(1080, 540, 500, 300, "Сводка за смену")
for i, (label, value, color) in enumerate([("Открыто", 5, YELLOW), ("Решено", 12, GREEN), ("Эскалировано", 2, RED)]):
    x = 1100 + i * 160
    box(x, 600, 140, 160, fill="#FFFFFF", stroke=color, rounded=1, extra="strokeWidth=3;arcSize=6;")
    box(x, 620, 140, 70, str(value), font=48, bold=True, color=color if color != YELLOW else "#F57F17")
    box(x, 700, 140, 40, label, font=15)
box(1100, 780, 460, 40, "Всего за смену: 19 обращений · среднее время решения 2 ч 10 мин",
    font=12, color="#546E7A", align="left")

# Легенда
box(20, 860, 1560, 60, fill="#FFFFFF", stroke="#B0BEC5", rounded=1, extra="arcSize=8;")
box(40, 870, 120, 40, "Легенда:", font=15, bold=True, align="left")
for i, (color, text) in enumerate([(GREEN, "Норма (CPU/RAM до 70 %, сервис доступен)"),
                                   (YELLOW, "Предупреждение (70–85 %, деградация)"),
                                   (RED, "Критично (свыше 85 %, сервис недоступен, P1)")]):
    x = 160 + i * 420
    circle(x, 878, 24, color)
    box(x + 32, 870, 380, 40, text, font=14, align="left")
line(1430, 890, 1480, 890, RED, dashed=True, width=2)
box(1485, 870, 90, 40, "порог", font=14, align="left")

xml = ('<mxfile host="app.diagrams.net"><diagram id="dashboard" name="Дашборд">'
       '<mxGraphModel dx="1600" dy="940" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" '
       'fold="1" page="1" pageScale="1" pageWidth="1600" pageHeight="940" math="0" shadow="0"><root>'
       '<mxCell id="0"/><mxCell id="1" parent="0"/>' + "".join(cells) + '</root></mxGraphModel></diagram></mxfile>')
with open(OUT, "w", encoding="utf-8") as f:
    f.write(xml)
print(f"{len(cells)} элементов → {OUT}")
