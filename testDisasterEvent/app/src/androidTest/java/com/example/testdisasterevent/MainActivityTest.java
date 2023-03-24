package com.example.testdisasterevent;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

public class MainActivityTest {
    private static final String TEST_ACCOUNT_USER_INFO_JSON = "{\"username\": \"testuser\", \"email\": \"testuser@example.com\"}";

    @Before
    public void setUp() {
        // Launch the activity with a test intent that contains accountUserInfoJson extra
        Intent intent = new Intent();
        intent.putExtra("accountUserInfoJson", TEST_ACCOUNT_USER_INFO_JSON);
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testNavigation() {
        // Check that the navigation view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.nav_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check that the home destination is selected by default
        Espresso.onView(ViewMatchers.withId(R.id.navigation_home)).check(ViewAssertions.matches(ViewMatchers.isSelected()));

        // Navigate to the notifications destination
        Espresso.onView(ViewMatchers.withId(R.id.navigation_notifications)).perform(ViewActions.click());

        // Check that the notifications destination is selected
        Espresso.onView(ViewMatchers.withId(R.id.navigation_notifications)).check(ViewAssertions.matches(ViewMatchers.isSelected()));

        // Navigate to the account destination
        Espresso.onView(ViewMatchers.withId(R.id.navigation_account)).perform(ViewActions.click());

        // Check that the account destination is selected
        Espresso.onView(ViewMatchers.withId(R.id.navigation_account)).check(ViewAssertions.matches(ViewMatchers.isSelected()));

        // Navigate to the home destination
        Espresso.onView(ViewMatchers.withId(R.id.navigation_home)).perform(ViewActions.click());

        // Check that the home destination is selected again
        Espresso.onView(ViewMatchers.withId(R.id.navigation_home)).check(ViewAssertions.matches(ViewMatchers.isSelected()));
    }


}
