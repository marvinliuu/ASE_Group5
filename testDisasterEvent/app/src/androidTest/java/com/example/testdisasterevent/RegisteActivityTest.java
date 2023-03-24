package com.example.testdisasterevent;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.testdisasterevent.ui.login.LoginActivity;
import com.example.testdisasterevent.ui.register.RegisterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class RegisteActivityTest {
    @Rule
    public ActivityScenarioRule<RegisterActivity> activityScenarioRule
            = new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void testValidRegistration() {
        onView(withId(R.id.registerName)).perform(typeText("John Doe"));
        onView(withId(R.id.registerEmail)).perform(typeText("johndoe@example.com"));
        onView(withId(R.id.registerPassword)).perform(typeText("P@ssw0rd"));
        onView(withId(R.id.registerActivationCode)).perform(typeText("111"));
        onView(withId(R.id.registerFinish)).perform(click());
        onView(withId(R.id.container)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegistrationWithDefaultActdode() {
        onView(withId(R.id.registerName)).perform(typeText("John Doe"));
        onView(withId(R.id.registerEmail)).perform(typeText("johndoe@example.com"));
        onView(withId(R.id.registerPassword)).perform(typeText("P@ssw0rd"));

        onView(withId(R.id.registerFinish)).perform(click());
        onView(withId(R.id.container)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegistrationBack() {
        onView(withId(R.id.registerName)).perform(typeText("John Doe"));
        onView(withId(R.id.registerEmail)).perform(typeText("johndoe@example.com"));
        onView(withId(R.id.registerPassword)).perform(typeText("P@ssw0rd"));
        onView(withId(R.id.registerBack)).perform(click());
        onView(withId(R.id.app_logo)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegistrationPasswordVisiable() {
        onView(withId(R.id.registerPassword)).perform(typeText("P@ssw0rd"));
        onView(withId(R.id.passwordVisible)).perform(click());
        onView(withId(R.id.registerPassword)).check(matches(withText("P@ssw0rd")));

    }

    @Test
    public void testRegistrationHelp() throws InterruptedException {
        onView(withId(R.id.ActivationExplanation)).check(matches(isDisplayed()));
        onView(withId(R.id.ActivationExplanation)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(3000);

        onView(withId(R.id.tv_pop_name)).check(matches(withText("Activation Code")));
        onView(withId(R.id.tv_pop_content)).check(matches(withText("@string/activation_code")));

    }
}
