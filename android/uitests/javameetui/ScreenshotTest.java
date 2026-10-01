import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Instrumentation;
import android.graphics.Bitmap;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;

// Автоматический сценарий: нажимает виджеты и сохраняет снимки экрана
// во внутреннюю папку приложения files/shots (оттуда их забирает задача screenshots)
@RunWith(AndroidJUnit4.class)
public class ScreenshotTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    private void shot(String name) throws Exception {
        Instrumentation instr = InstrumentationRegistry.getInstrumentation();
        instr.waitForIdleSync();
        Thread.sleep(800);
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
        onView(withId(R.id.btnAdd)).perform(click());
        onView(withId(R.id.btnAdd)).perform(click());
        onView(withId(R.id.btnAdd)).perform(click());
        onView(withId(R.id.btnTake)).perform(click());
        shot("2_add_take");

        // GROW → текст растянут по горизонтали
        onView(withId(R.id.btnGrow)).perform(click());
        shot("3_grow");

        // HIDE → число скрыто, кнопка стала SHOW
        onView(withId(R.id.btnHide)).perform(click());
        shot("4_hide");
    }
}
