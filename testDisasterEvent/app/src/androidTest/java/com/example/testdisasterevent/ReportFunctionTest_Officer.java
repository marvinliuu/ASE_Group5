package com.example.testdisasterevent;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.testdisasterevent.EspressoUtils.setProgress;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class ReportFunctionTest_Officer {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void ReportInfoShow() {
        onView(withId(R.id.navigation_notifications)).perform(click());

        // check the layout and controls
        onView(ViewMatchers.withId(R.id.report_container))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_top))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withText("Report a disaster"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withText("this report will be sibmuit to Garda"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_type))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_radioGroup))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withText("Fire"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withText("Water"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withText("Other"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_where))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withText("Where is the disaster:"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withText("mark from the map"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_mark_map_icon))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_other))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withText("How many people injured:"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_injured))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withText("Updated information"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_otherinfo))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_btn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_cancel))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_submit))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }
    @Test
    public void testReportMapConfirm() {
        onView(withId(R.id.navigation_notifications)).perform(click());

        onView(withId(R.id.report_mark_map_icon)).perform(click());

        onView(withId(R.id.report_map)).check(matches(isDisplayed()));
        onView(withId(R.id.R_map)).check(matches(isDisplayed()));
        onView(withId(R.id.reportMap_cancel)).check(matches(isDisplayed()));
        onView(withId(R.id.reportMap_submit)).check(matches(isDisplayed()));
        onView(withId(R.id.radius_dialog)).check(matches(isDisplayed()));
        onView(withId(R.id.mSeekBar)).check(matches(isDisplayed()));
        onView(withId(R.id.radius_text)).check(matches(withText("Radius")));

        onView(withId(R.id.mSeekBar)).check(matches(isDisplayed()));
        onView(withId(R.id.mSeekBar)).perform(setProgress(50));

        // Test submit button
        onView(withId(R.id.reportMap_submit)).perform(click());

        // check the layout and controls
        onView(ViewMatchers.withId(R.id.report_container))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testReportMapCancel() {
        onView(withId(R.id.navigation_notifications)).perform(click());

        onView(withId(R.id.report_mark_map_icon)).perform(click());

        onView(withId(R.id.report_map)).check(matches(isDisplayed()));
        onView(withId(R.id.R_map)).check(matches(isDisplayed()));
        onView(withId(R.id.reportMap_cancel)).check(matches(isDisplayed()));
        onView(withId(R.id.reportMap_submit)).check(matches(isDisplayed()));
        onView(withId(R.id.radius_dialog)).check(matches(isDisplayed()));
        onView(withId(R.id.mSeekBar)).check(matches(isDisplayed()));
        onView(withId(R.id.radius_text)).check(matches(withText("Radius")));

        // Test cancel button
        onView(withId(R.id.reportMap_cancel)).perform(click());

        // check the layout and controls
        onView(ViewMatchers.withId(R.id.report_container))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testReportSubmission() {
        onView(withId(R.id.navigation_notifications)).perform(click());

        // check the layout and controls
        onView(withId(R.id.report_container))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Click on the "Water" radio button
        onView(withId(R.id.report_water)).perform(click());

        // Click on the map button and set the radius
        onView(withId(R.id.report_mark_map_icon)).perform(click());

        onView(withId(R.id.mSeekBar)).check(matches(isDisplayed()));
        onView(withId(R.id.mSeekBar)).perform(setProgress(50));

        // Test submit button
        onView(withId(R.id.reportMap_submit)).perform(click());


        // Enter the number of injured people
        onView(withId(R.id.report_injured)).perform(typeText("5"));

        // Enter additional information
        onView(withId(R.id.report_otherinfo)).perform(typeText("Additional information about the water disaster"));

        onView(ViewMatchers.withId(R.id.report_submit)).perform(click());

        // Check if the TextView with the text "Report submit successfully" is displayed
        onView(withText("Report submit successfully")).check(matches(isDisplayed()));

    }

}
