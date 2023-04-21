package com.example.testdisasterevent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.espresso.matcher.RootMatchers.*;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class DisasterPopupWindowTest {
    @Rule
    public ActivityTestRule<MainActivity> ActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void DisasterInfoShow() throws InterruptedException {
        onView(withId(R.id.navigation_dashboard)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(3000);

        // check the controls in disaster popupwindow
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).check(matches(isDisplayed()));
    }

    @Test
    public void DisasterInfoWindowClose() throws InterruptedException {
        onView(ViewMatchers.withId(R.id.nav_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.navigation_dashboard)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(3000);

        //find and colse the DisasterInfo popupwindow
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void DisasterInfoShowMannually() throws InterruptedException {
        onView(withId(R.id.navigation_dashboard)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(5000);

        //close and reopen the DisasterInfo popupwindow
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).perform(click());

        onView(withId(R.id.show_popwindow)).check(matches(isDisplayed()));
        onView(withId(R.id.show_popwindow)).perform(click());

        //find and colse the DisasterInfo popupwindow
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void DisasterDetailsShow() throws InterruptedException {
        onView(withId(R.id.navigation_dashboard)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(5000);

        //close and reopen the DisasterInfo popupwindow
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).perform(click());

        onView(withId(R.id.show_popwindow)).check(matches(isDisplayed()));
        onView(withId(R.id.show_popwindow)).perform(click());
    }
}

