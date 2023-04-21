package com.example.testdisasterevent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.rule.ActivityTestRule;


import com.example.testdisasterevent.data.model.AccountUserInfo;

import org.junit.Rule;
import org.junit.Test;

public class TaskPopupwindowTest {
    @Rule
    public androidx.test.rule.ActivityTestRule<MainActivity> ActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void TaskInfoShow() throws InterruptedException {
        onView(withId(R.id.navigation_dashboard)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(3000);

        // check the layout and controls
        onView(withId(R.id.disaster_logo)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn1)).check(matches(isDisplayed()));
        onView(withId(R.id.type_intro)).check(matches(isDisplayed()));
        onView(withId(R.id.location_intro)).check(matches(isDisplayed()));
        onView(withId(R.id.ftime_intro)).check(matches(isDisplayed()));
        onView(withId(R.id.task_intro)).check(matches(isDisplayed()));
    }

    @Test
    public void TaskInfoWindowClose() throws InterruptedException {
        onView(withId(R.id.navigation_dashboard)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(3000);

        //colse the DisasterInfo popupwindow
        onView(withId(R.id.close_btn1)).perform(click());

        // check whether the layout is disaster page
        onView(withId(R.id.disaster_container)).check(matches(isDisplayed()));
    }

    @Test
    public void TaskInfoShowMannually() throws InterruptedException {
        onView(withId(R.id.navigation_dashboard)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(5000);

        //close and reopen the DisasterInfo popupwindow
        onView(withId(R.id.close_btn1)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn1)).perform(click());
        onView(withId(R.id.show_taskdetails)).check(matches(isDisplayed()));
        onView(withId(R.id.show_taskdetails)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(5000);

        // check the layout and controls
        onView(withId(R.id.disaster_logo)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn1)).check(matches(isDisplayed()));
        onView(withId(R.id.type_intro)).check(matches(isDisplayed()));
        onView(withId(R.id.location_intro)).check(matches(isDisplayed()));
        onView(withId(R.id.ftime_intro)).check(matches(isDisplayed()));
        onView(withId(R.id.task_intro)).check(matches(isDisplayed()));
    }

}
