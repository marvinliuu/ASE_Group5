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

public class TaskPopupwindowTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void TaskInfoShow() throws InterruptedException {
        onView(withId(R.id.navigation_dashboard)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(3000);

        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn1)).check(matches(isDisplayed()));
    }

    @Test
    public void TaskInfoWindowClose() throws InterruptedException {
        onView(withId(R.id.navigation_home)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(3000);

        //colse the DisasterInfo popupwindow
        onView(withId(R.id.close_btn1)).perform(click());;
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void TaskInfoShowMannually() throws InterruptedException {
        onView(withId(R.id.navigation_home)).perform(click());

        // wait for 5 seconds until PopupWindow shows
        Thread.sleep(5000);

        //close and reopen the DisasterInfo popupwindow
        onView(withId(R.id.close_btn1)).perform(click());
        onView(withId(R.id.show_popwindow)).perform(click());

        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
    }
}
