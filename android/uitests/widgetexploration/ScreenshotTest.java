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
        Thread.sleep(5000);   // даём WebView загрузить сайт
        shot("1_start");

        onView(withId(R.id.radioButtonBeijing)).perform(click());
        onView(withId(R.id.checkBoxTint)).perform(click());
        onView(withId(R.id.checkBoxReSize)).perform(click());
        onView(withId(R.id.editText)).perform(replaceText("Привет, Android!"));
        closeSoftKeyboard();
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.switch1)).perform(click());
        shot("2_widgets");

        onView(withId(R.id.checkBoxTransparency)).perform(click());
        onView(withId(R.id.radioButtonNewYork)).perform(click());
        shot("3_transparency");
    }
}
