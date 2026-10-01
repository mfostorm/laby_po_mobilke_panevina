"""Компилирует код лабораторных против android.jar и запускает Log-приложения
на обычной JVM (с заглушками android.util.Log и т.п.), сохраняя вывод в out/."""
import os, re, subprocess, sys, glob, shutil, tempfile

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ANDROID_JAR = os.environ.get("ANDROID_JAR")
OUT = os.path.join(ROOT, "tools", "out")
os.makedirs(OUT, exist_ok=True)

COMPILE_STUBS = {
    "androidx/appcompat/app/AppCompatActivity.java":
        "package androidx.appcompat.app;\npublic class AppCompatActivity extends android.app.Activity {}\n",
}
RUN_STUBS = {
    "android/util/Log.java": """package android.util;
public final class Log {
    static void p(String t, String m) { System.out.println(t.endsWith(":") ? t + " " + m : t + ": " + m); }
    public static int i(String t, String m) { p(t, m); return 0; }
    public static int d(String t, String m) { p(t, m); return 0; }
    public static int w(String t, String m) { p(t, m); return 0; }
    public static int e(String t, String m) { p(t, m); return 0; }
}""",
    "android/os/Bundle.java": "package android.os;\npublic class Bundle {}\n",
    "androidx/appcompat/app/AppCompatActivity.java": """package androidx.appcompat.app;
public class AppCompatActivity {
    protected void onCreate(android.os.Bundle b) {}
    public void setContentView(int id) {}
}""",
}

def write(base, files):
    for rel, src in files.items():
        p = os.path.join(base, rel)
        os.makedirs(os.path.dirname(p), exist_ok=True)
        open(p, "w").write(src)

def make_r(base, pkg, app_dir):
    ids = set()
    for x in glob.glob(os.path.join(app_dir, "*.xml")):
        ids |= set(re.findall(r'@\+id/(\w+)', open(x).read()))
    body = "".join(f"public static final int {n} = {i + 1000};" for i, n in enumerate(sorted(ids)))
    src = f"package {pkg};\npublic final class R {{ public static final class layout {{ public static final int activity_main = 1; }}\npublic static final class id {{ {body} }} }}\n"
    write(base, {pkg.replace('.', '/') + "/R.java": src})

def javac(srcs, cp, outdir):
    cmd = ["javac", "-encoding", "UTF-8", "-nowarn", "-d", outdir] + (["-cp", cp] if cp else []) + srcs
    r = subprocess.run(cmd, capture_output=True, text=True)
    return r.returncode, r.stdout + r.stderr

def app_dirs():
    for f in sorted(glob.glob(os.path.join(ROOT, "**", "MainActivity.java"), recursive=True)):
        yield os.path.dirname(f)

ok = True
for d in app_dirs():
    rel = os.path.relpath(d, ROOT)
    srcs = sorted(glob.glob(os.path.join(d, "*.java")))
    pkg = re.search(r'^package ([\w.]+);', open(srcs[0]).read(), re.M).group(1)
    uses_ui = "android.widget" in "".join(open(s).read() for s in srcs)
    with tempfile.TemporaryDirectory() as t:
        stubs = os.path.join(t, "stubs"); classes = os.path.join(t, "cls"); os.makedirs(classes)
        # 1) Проверка против настоящего android.jar
        write(stubs, COMPILE_STUBS); make_r(stubs, pkg, d)
        code, msg = javac(srcs + glob.glob(stubs + "/**/*.java", recursive=True), ANDROID_JAR, classes)
        print(("OK   " if code == 0 else "FAIL ") + rel + "  [android.jar]")
        if code: print(msg); ok = False; continue
        if uses_ui:
            continue
        # 2) Запуск на JVM с заглушками
        shutil.rmtree(stubs); shutil.rmtree(classes); os.makedirs(classes)
        write(stubs, RUN_STUBS); make_r(stubs, pkg, d)
        runner = f"""package {pkg};
public class Runner {{ public static void main(String[] a) throws Exception {{
    java.lang.reflect.Method m = MainActivity.class.getDeclaredMethod("onCreate", android.os.Bundle.class);
    m.setAccessible(true); m.invoke(new MainActivity(), (Object) null); }} }}"""
        write(stubs, {pkg.replace('.', '/') + "/Runner.java": runner})
        code, msg = javac(srcs + glob.glob(stubs + "/**/*.java", recursive=True), None, classes)
        if code: print(msg); ok = False; continue
        r = subprocess.run(["java", "-Dfile.encoding=UTF-8", "-cp", classes, pkg + ".Runner"], capture_output=True, text=True)
        name = rel.replace(os.sep, "__").replace(" ", "_") + ".txt"
        open(os.path.join(OUT, name), "w").write(r.stdout)
        if r.returncode: print(r.stderr); ok = False
# Консольные программы (задания): папки код/ с методом main и без MainActivity
for d in sorted(set(os.path.dirname(f) for f in glob.glob(os.path.join(ROOT, "**", "*.java"), recursive=True))):
    srcs = sorted(glob.glob(os.path.join(d, "*.java")))
    if any(os.path.basename(s) == "MainActivity.java" for s in srcs):
        continue
    mains = [s for s in srcs if "static void main" in open(s).read()]
    if not mains:
        continue
    rel = os.path.relpath(d, ROOT)
    with tempfile.TemporaryDirectory() as t:
        code, msg = javac(srcs, None, t)
        print(("OK   " if code == 0 else "FAIL ") + rel + "  [java]")
        if code: print(msg); ok = False; continue
        main = os.path.basename(mains[0])[:-5]
        r = subprocess.run(["java", "-Dstdout.encoding=UTF-8", "-cp", t, main], capture_output=True, text=True)
        name = rel.replace(os.sep, "__").replace(" ", "_") + ".txt"
        open(os.path.join(OUT, name), "w").write(r.stdout)
        if r.returncode: print(r.stderr); ok = False
sys.exit(0 if ok else 1)
