package com.example.testdisasterevent;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import static androidx.test.espresso.Espresso.onView;
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
        onView(ViewMatchers.withId(R.id.nav_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check that the home destination is selected by default
        onView(ViewMatchers.withId(R.id.navigation_home)).check(ViewAssertions.matches(ViewMatchers.isSelected()));

        // Navigate to the notifications destination
        onView(ViewMatchers.withId(R.id.navigation_notifications)).perform(ViewActions.click());

        // Check that the notifications destination is selected
        onView(ViewMatchers.withId(R.id.navigation_notifications)).check(ViewAssertions.matches(ViewMatchers.isSelected()));

        // Navigate to the account destination
        onView(ViewMatchers.withId(R.id.navigation_account)).perform(ViewActions.click());

        // Check that the account destination is selected
        onView(ViewMatchers.withId(R.id.navigation_account)).check(ViewAssertions.matches(ViewMatchers.isSelected()));

        // Navigate to the home destination
        onView(ViewMatchers.withId(R.id.navigation_home)).perform(ViewActions.click());

        // Check that the home destination is selected again
        onView(ViewMatchers.withId(R.id.navigation_home)).check(ViewAssertions.matches(ViewMatchers.isSelected()));
    }


}
