package com.example.testdisasterevent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.rule.ActivityTestRule;

import com.example.testdisasterevent.ui.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;

public class Identification_Officer {
    @Rule
    public ActivityTestRule<LoginActivity> loginActivityTestRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void OfficerAccountIdentification() throws InterruptedException {
        onView(withId(R.id.username)).perform(typeText("aaa@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("aaaaaa"), closeSoftKeyboard());

        // Click on the login button
        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);

        //colse the DisasterInfo popupwindow
        onView(withId(R.id.close_btn)).perform(click());

        onView(withId(R.id.navigation_account)).perform(click());

        // check the information details
        onView(withId(R.id.account_login_info)).check(matches(isDisplayed()));
        onView(withId(R.id.account_type_text)).check(matches(withText("Police")));
        onView(withId(R.id.account_name_info)).check(matches(withText("aaaaaa")));
        onView(withId(R.id.account_email_info)).check(matches(withText("aaa@gmail.com")));
        onView(withId(R.id.account_mobile_info)).check(matches(withText("123456")));
    }
}
