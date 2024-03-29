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

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class ReportFunctionTest_Officer {
    @Rule
    public androidx.test.rule.ActivityTestRule<MainActivity> ActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ReportUITest() {
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
        // Click on the notification button
        onView(withId(R.id.navigation_notifications)).perform(click());

        // Check if the report container is displayed
        onView(withId(R.id.report_container));

        // Click on the map marker icon
        onView(withId(R.id.report_mark_map_icon)).perform(click());

        // Check if the seek bar is displayed
        onView(withId(R.id.mSeekBar)).check(matches(isDisplayed()));

        // Set the progress of the seek bar to 50
        onView(withId(R.id.mSeekBar)).perform(setProgress(50));

        // Click on the submit button for the map
        onView(withId(R.id.reportMap_submit)).perform(click());

        // Click on the water disaster option
        onView(withId(R.id.report_water)).perform(click());

        // Type the number of injured people
        onView(withId(R.id.report_injured)).perform(typeText("5"));

        // Type additional information about the disaster
        onView(withId(R.id.report_otherinfo)).perform(typeText("Additional information about the water disaster"));

        // Close the soft keyboard
        closeSoftKeyboard();

        // Click on the submit button for the report
        onView(ViewMatchers.withId(R.id.report_submit)).perform(click());

        // Check if the success message is displayed
        onView(withText("Report submit successfully")).check(matches(isDisplayed()));

    }

}
