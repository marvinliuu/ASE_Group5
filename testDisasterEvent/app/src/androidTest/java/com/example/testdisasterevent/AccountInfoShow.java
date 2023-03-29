package com.example.testdisasterevent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;



import org.junit.Rule;
import org.junit.Test;

public class AccountInfoShow {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

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
