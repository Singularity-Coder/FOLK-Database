package com.singularitycoder.folkdatabase.helpers;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.espresso.FailureHandler;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.singularitycoder.folkdatabase.R;
import com.singularitycoder.folkdatabase.auth.view.MainActivity;

import junit.framework.AssertionFailedError;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class DummyTest {

    // Test UI elements that are important to the user
    // Espresso prevents direct access to activities n views

    private static final String TAG = "MainActivityUiTest";

    private MainActivity mainActivity;
    private EditText etName, etEmail, etPhone, etPassword;
    private Button btnCreateAccount;
    private IdlingResource idlingResource;
    private CountingIdlingResource countingIdlingResource;
    private UiDevice uiDevice;

    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    public void setUp() throws Exception {
        mainActivity = activityTestRule.getActivity();

//        idlingResource = activityTestRule.getActivity().getWaitingState();
        IdlingRegistry.getInstance().register(idlingResource);

        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

//        etName = mainActivity.findViewById(R.id.et_name);
//        etEmail = mainActivity.findViewById(R.id.et_email);
//        etPhone = mainActivity.findViewById(R.id.et_phone);
//        etPassword = mainActivity.findViewById(R.id.et_password);

        btnCreateAccount = mainActivity.findViewById(R.id.btn_create_account);
    }

    public void loginTest() throws Exception {
//        onView(withId(R.id.et_name))
//                .perform(typeText("Hithesh"))
//                .check(matches(withText("Hithesh")));
//        onView(withId(R.id.et_email))
//                .perform(typeText("hithesh@gmail.com"))
//                .check(matches(withText("hithesh@gmail.com")));
//        onView(withId(R.id.et_phone))
//                .perform(typeText("hitWonder"))
//                .check(matches(withText("hitWonder")));
//        onView(withId(R.id.et_password))
//                .perform(typeText("hitWonder"))
//                .check(matches(withText("hitWonder")));
//        onView(withId(R.id.btn_create_account))
//                .perform(click());
//        onView(withId(R.id.btn_create_account))
//                .check(matches(withText("Success")));
    }

    public boolean isValidMail(String mail) {
        return Patterns.EMAIL_ADDRESS.matcher(mail).matches();
    }

    public void statusBarColor_onCreate_darker() {
        try {
            onView(withText("Button")).perform(click());
            // View is in hierarchy

        } catch (NoMatchingViewException e) {
            // View is not in hierarchy
        }


        // check if view is displayed on screen or not
        try {
            onView(withText("Button")).check(matches(isDisplayed()));
            // View is displayed
        } catch (AssertionFailedError e) {
            // View not displayed
        }

        // check if not visible on screen
        onView(withId(R.id.tv_no_internet)).check(matches(not(isDisplayed())));

        // if scrollview n not displayed yet
        onView(withId(R.id.tv_no_internet)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
        onView(withId(R.id.tv_no_internet)).check(matches(withEffectiveVisibility(Visibility.GONE)));
        onView(withId(R.id.tv_no_internet)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)));

        // check if any of the views is visible
        onView(anyOf(withId(R.id.tv_no_internet), withId(R.id.tv_no_internet)))
                .check(matches(isDisplayed()));

//        onView(withId(R.id.progressBar)).check(matches(isDisplayed()));

        // confirm that this view doesnt exist. It should not exist. not gone or invisible
        onView(withId(R.id.tv_no_internet)).check(doesNotExist());

        final AtomicBoolean view1Displayed = new AtomicBoolean(true);
        onView(withId(R.id.tv_no_internet))
                .inRoot(withDecorView(is(activityTestRule.getActivity().getWindow().getDecorView())))
                .withFailureHandler(new FailureHandler() {
                    @Override
                    public void handle(Throwable error, org.hamcrest.Matcher<View> viewMatcher) {
                        view1Displayed.set(false);
                    }
                }).check(ViewAssertions.matches(isDisplayed()));

        if (view1Displayed.get()) {
            try {
                onView(withId(R.id.tv_no_internet))
                        .inRoot(withDecorView(is(activityTestRule.getActivity().getWindow().getDecorView())))
                        .check(ViewAssertions.matches(not(isDisplayed())));
            } catch (NoMatchingViewException ignore) {
            }
        } else {
            onView(withId(R.id.tv_no_internet))
                    .inRoot(withDecorView(is(activityTestRule.getActivity().getWindow().getDecorView())))
                    .check(ViewAssertions.matches(isDisplayed()));
        }

        // dialogs
        onView(withText("dialogText")).check(matches(isDisplayed()));
        onView(withId(R.id.tv_no_internet)).check(matches(allOf(withText("dialog text"), isDisplayed())));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withText("Dialog"))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));

    }

    private void uiAutomator() throws UiObjectNotFoundException {
        // Search for correct button in the dialog.
        UiObject button = uiDevice.findObject(new UiSelector().text("ButtonText"));
        if (button.exists() && button.isEnabled()) {
            button.click();
        }
    }

    public void statusBarColor_onCreate_colorDark() {
//        int type = root.getWindowLayoutParams().get().type;
//        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
//            IBinder windowToken = root.getDecorView().getWindowToken();
//            IBinder appToken = root.getDecorView().getApplicationWindowToken();
//            if (windowToken == appToken) {
//                // windowToken == appToken means this window isn't contained by any other windows.
//                // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
//                return true;
//            }
//        }
    }

    private void dialogs() {
        // android.R.id.button1, android.R.id.button2 and android.R.id.button3 for "positive", "neutral" and "negative", are global symbols
        // For anything outside your UI, UIAutomator is recommended
        int titleId = activityTestRule.getActivity().getResources()
                .getIdentifier("android.R.id.alertTitle", "id", "android");

        onView(withId(titleId))
                .inRoot(isDialog())
                .check(matches(withText("R.string.my_title")))
                .check(matches(isDisplayed()));

        onView(withId(android.R.id.text1))
                .inRoot(isDialog())
                .check(matches(withText("R.string.my_message")))
                .check(matches(isDisplayed()));

        onView(withId(android.R.id.button2))
                .inRoot(isDialog())
                .check(matches(withText(android.R.string.no)))
                .check(matches(isDisplayed()));

        onView(withId(android.R.id.button3))
                .inRoot(isDialog())
                .check(matches(withText(android.R.string.yes)))
                .check(matches(isDisplayed()));

        onView(withId(android.R.id.button3)).perform(click());
    }

    public static class LiveDataTestUtil {
        public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
            final Object[] data = new Object[1];
            final CountDownLatch latch = new CountDownLatch(1);
            Observer<T> observer = new Observer<T>() {
                @Override
                public void onChanged(@Nullable T o) {
                    data[0] = o;
                    latch.countDown();
                    liveData.removeObserver(this);
                }
            };
            liveData.observeForever(observer);
            // Don't wait indefinitely if the LiveData is not set.
            if (!latch.await(2, TimeUnit.SECONDS)) {
                throw new RuntimeException("LiveData value was never set.");
            }
            //noinspection unchecked
            return (T) data[0];
        }
    }

    public void tearDown() throws Exception {
        if (null != idlingResource) IdlingRegistry.getInstance().unregister(idlingResource);
        mainActivity = null;
    }
}