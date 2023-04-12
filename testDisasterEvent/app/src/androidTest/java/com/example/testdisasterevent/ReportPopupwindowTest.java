package com.example.testdisasterevent;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.testdisasterevent.data.model.AccountUserInfo;

import org.junit.Rule;
import org.junit.Test;
public class ReportPopupwindowTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void ReportInfoShow() throws InterruptedException {
        onView(withId(R.id.navigation_home)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(3000);

        // check the layout and controls
        onView(withId(R.id.disaster_logo)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).check(matches(isDisplayed()));
    }

    @Test
    public void ReportInfoWindowClose() throws InterruptedException {
        onView(withId(R.id.navigation_home)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(3000);

        //colse the DisasterInfo popupwindow
        onView(withId(R.id.close_btn)).perform(click());

    }

    @Test
    public void ReportInfoShowMannually() throws InterruptedException {
        onView(withId(R.id.navigation_home)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(5000);

        //close and reopen the DisasterInfo popupwindow
        onView(withId(R.id.close_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).perform(click());
        onView(withId(R.id.show_popwindow)).check(matches(isDisplayed()));
        onView(withId(R.id.show_popwindow)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(5000);

        // check the layout and controls
        onView(withId(R.id.disaster_logo)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).check(matches(isDisplayed()));
    }
}
