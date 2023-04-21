package com.example.testdisasterevent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AccountInfoShow {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void AccountInfoShow() {
        onView(withId(R.id.navigation_account)).perform(click());

        // check the information details
        onView(withId(R.id.account_login_info)).check(matches(isDisplayed()));
        onView(withId(R.id.account_login_name)).check(matches(isDisplayed()));
        onView(withId(R.id.account_login_email)).check(matches(isDisplayed()));
        onView(withId(R.id.account_login_mobile)).check(matches(isDisplayed()));
    }
}

