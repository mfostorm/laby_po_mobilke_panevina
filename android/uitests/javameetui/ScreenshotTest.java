import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.widget.EditText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;

// Автоматический сценарий: нажимает виджеты (performClick — то же, что нажатие
// пальцем, вызываются те же слушатели) и сохраняет снимки экрана во внутреннюю
// папку приложения files/shots, откуда их забирает задача screenshots
@RunWith(AndroidJUnit4.class)
public class ScreenshotTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    private void click(int id) {
        rule.getScenario().onActivity(a -> a.findViewById(id).performClick());
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    private void type(int id, String text) {
        rule.getScenario().onActivity(a -> ((EditText) a.findViewById(id)).setText(text));
    }

    private void shot(String name) throws Exception {
        Instrumentation instr = InstrumentationRegistry.getInstrumentation();
        instr.waitForIdleSync();
        Thread.sleep(1000);
        Bitmap bmp = instr.getUiAutomation().takeScreenshot();
        File dir = new File(instr.getTargetContext().getFilesDir(), "shots");
        dir.mkdirs();
        try (FileOutputStream out = new FileOutputStream(new File(dir, name + ".png"))) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        }
    }

    @Test
    public void screens() throws Exception {
        shot("1_start");

        // ADD ×3, TAKE ×1 → 2
        click(R.id.btnAdd);
        click(R.id.btnAdd);
        click(R.id.btnAdd);
        click(R.id.btnTake);
        shot("2_add_take");

        // GROW → текст растянут по горизонтали
        click(R.id.btnGrow);
        shot("3_grow");

        // HIDE → число скрыто, кнопка стала SHOW
        click(R.id.btnHide);
        shot("4_hide");
    }
}
