package com.example.testdisasterevent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class RerouteFunctionTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void RerouteInfoShow() throws InterruptedException {
        onView(withId(R.id.navigation_home)).perform(click());

        // check the layout
        onView(withId(R.id.map)).check(matches(isDisplayed()));
        onView(withId(R.id.start_location)).check(matches(isDisplayed()));
        onView(withId(R.id.des_location)).check(matches(isDisplayed()));
        onView(withId(R.id.enter_btn)).check(matches(isDisplayed()));

    }

    @Test
    public void Reroute() {

        onView(withId(R.id.navigation_home)).perform(click());

        // Find the starting location input box
        onView(withId(R.id.start_location)).perform(click());

        // Enter the starting location in the input box
        onView(withId(R.id.start_location)).perform(typeText("100 Main St."));

        // Close the keyboard
        closeSoftKeyboard();

        // Find the destination location input box
        onView(withId(R.id.des_location)).perform(click());

        // Enter the destination location in the input box
        onView(withId(R.id.des_location)).perform(typeText("200 Market St."));

        // Close the keyboard
        closeSoftKeyboard();

        // Click the search button
        onView(withId(R.id.enter_btn)).perform(click());

        // Check if the starting location and destination location are already displayed on the map
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }
}
