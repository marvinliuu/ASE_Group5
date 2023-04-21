package com.example.testdisasterevent;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.core.app.ActivityScenario.*;
import static androidx.test.espresso.matcher.RootMatchers.isSystemAlertWindow;


import androidx.test.rule.ActivityTestRule;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


import android.app.Activity;
import android.view.View;
import android.view.Window;

import org.junit.Rule;
import org.junit.Test;

public class BusStopInfoShow {
    @Rule
    public ActivityTestRule<MainActivity> ActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void BusStopLayoutShow() throws InterruptedException {
        onView(withId(R.id.navigation_home)).perform(click());

        // check the layout
        onView(withId(R.id.map)).check(matches(isDisplayed()));
        onView(withId(R.id.start_location)).check(matches(isDisplayed()));
        onView(withId(R.id.get_busInfo)).check(matches(isDisplayed()));

    }

    @Test
    public void BusStopInfoShow() {

        onView(withId(R.id.navigation_home)).perform(click());

        // press the busInfo button
        onView(withId(R.id.get_busInfo)).perform(click());

        onView(withText("Request Real Time Bus Information"))
                .inRoot(isSystemAlertWindow())
                .check(matches(isDisplayed()));
    }
}
