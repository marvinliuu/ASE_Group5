package com.example.testdisasterevent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.testdisasterevent.ui.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest<ActivityTestRule> {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testValidLogin() {
        onView(withId(R.id.username)).perform(typeText("1@1.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("111111"), closeSoftKeyboard());

        // Click on the login button
        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.container)).check(matches(isDisplayed()));
    }


    @Test
    public void testInvalidLogin() {
        onView(withId(R.id.username)).perform(typeText("wwwwww"));
        onView(withId(R.id.password)).perform(typeText("111111111111"));

        // Click on the login button
        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.app_logo)).check(matches(isDisplayed()));
    }
}
