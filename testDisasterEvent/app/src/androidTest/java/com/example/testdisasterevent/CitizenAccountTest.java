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

public class CitizenAccountTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void CitizenAccountIdentification() throws InterruptedException {
        onView(withId(R.id.username)).perform(typeText("ggg@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("gggggg"), closeSoftKeyboard());

        // Click on the login button
        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);

        onView(withId(R.id.navigation_account)).perform(click());

        // check the information details
        onView(withId(R.id.account_login_info)).check(matches(isDisplayed()));
        onView(withId(R.id.account_type_text)).check(matches(withText("Citizen")));
        onView(withId(R.id.account_name_info)).check(matches(withText("gggggg")));
        onView(withId(R.id.account_email_info)).check(matches(withText("ggg@gmail.com")));
        onView(withId(R.id.account_mobile_info)).check(matches(withText("123456")));
    }
}
