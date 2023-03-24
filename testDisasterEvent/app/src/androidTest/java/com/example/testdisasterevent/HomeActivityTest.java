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
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.testdisasterevent.ui.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class HomeActivityTest {
    @Rule
    public ActivityScenarioRule<HomeActivity> activityScenarioRule =
            new ActivityScenarioRule<>(HomeActivity.class);


    @Test
    public void testUI() {
        // Check if the logo and gif are displayed
        onView(ViewMatchers.withId(R.id.app_logo))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(ViewMatchers.withId(R.id.welcome))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Click on the "Register" button
        onView(ViewMatchers.withId(R.id.sign_up))
                .perform(ViewActions.click());

    }
}
