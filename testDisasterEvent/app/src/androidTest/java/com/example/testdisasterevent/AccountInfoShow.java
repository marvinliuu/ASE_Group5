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

        onView(withId(R.id.account_name_info)).check(matches(isDisplayed()));
        onView(withId(R.id.account_email_info)).check(matches(isDisplayed()));
        onView(withId(R.id.account_mobile_info)).check(matches(isDisplayed()));
    }
}
