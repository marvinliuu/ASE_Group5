package com.example.testdisasterevent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;


import com.example.testdisasterevent.ui.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class HomeActivityTest {
    @Rule
    public ActivityTestRule<HomeActivity> ActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);


    @Test
    public void HomeActivityUITest() {
        // Check if app_logo ImageView exists
        onView(withId(R.id.app_logo)).check(matches(isDisplayed()));

        // Check if textView ImageView exists
        onView(withId(R.id.textView)).check(matches(isDisplayed()));

        // Check if welcome ImageView exists
        onView(withId(R.id.welcome)).check(matches(isDisplayed()));

        // Check if log_in Button exists and has the correct text
        onView(withId(R.id.log_in))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.action_sign_in)));

        // Check if sign_up TextView exists and has the correct text
        onView(withId(R.id.sign_up))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.action_register)));

    }
}
