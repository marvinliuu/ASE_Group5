package com.example.testdisasterevent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isSystemAlertWindow;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.testdisasterevent.ui.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
public class AppTest_Officer {
    @Rule
    public ActivityTestRule<LoginActivity> loginActivityTestRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void OfficerHomeTest() throws InterruptedException {
        // Login Test
        onView(withId(R.id.username)).perform(typeText("aaa@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("aaaaaa"), closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.container)).check(matches(isDisplayed()));


        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);

        //ReportInfo popupwindow Test
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);
        onView(withId(R.id.close_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).perform(click());

        // BusStopInfo Test
        onView(withId(R.id.get_busInfo)).perform(click());
        Thread.sleep(3000);

        // Reroute Function Test
        onView(withId(R.id.start_location)).perform(click());
        onView(withId(R.id.start_location)).perform(typeText("buswells"));
        closeSoftKeyboard();
        onView(withId(R.id.des_location)).perform(click());
        onView(withId(R.id.des_location)).perform(typeText("tara street"));
        closeSoftKeyboard();
        onView(withId(R.id.enter_btn)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));
        Thread.sleep(3000);
    }

    @Test
    public void OfficerAccountTest() throws InterruptedException {
        // Login Test
        onView(withId(R.id.username)).perform(typeText("aaa@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("aaaaaa"), closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);

        //ReportInfo popupwindow Test
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).perform(click());

        //colse the DisasterInfo popupwindow
        onView(withId(R.id.navigation_account)).perform(click());

        // check the information details
        onView(withId(R.id.account_login_info)).check(matches(isDisplayed()));
        onView(withId(R.id.account_type_text)).check(matches(withText("Police")));
        onView(withId(R.id.account_name_info)).check(matches(withText("gdfd")));
        onView(withId(R.id.account_email_info)).check(matches(withText("aaa@gmail.com")));
        onView(withId(R.id.account_mobile_info)).check(matches(withText("284644")));
    }

    @Test
    public void OfficerDisasterTest() throws InterruptedException {
        // Login Test
        onView(withId(R.id.username)).perform(typeText("aaa@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("aaaaaa"), closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);

        //ReportInfo popupwindow Test
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).perform(click());

        //Jump to Disaster page
        onView(withId(R.id.navigation_dashboard)).perform(click());

        // check the Task and Disaster popupwindow
        Thread.sleep(3000);

//        onView(withId(R.id.disaster_logo)).check(matches(isDisplayed()));
//        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
//        onView(withId(R.id.close_btn1)).check(matches(isDisplayed()));
//        onView(withId(R.id.type_intro)).check(matches(isDisplayed()));
//        onView(withId(R.id.location_intro)).check(matches(isDisplayed()));
//        onView(withId(R.id.ftime_intro)).check(matches(isDisplayed()));
//        onView(withId(R.id.task_intro)).check(matches(isDisplayed()));
//
//        onView(withId(R.id.close_btn1)).perform(click());

        onView(withId(R.id.show_popwindow)).check(matches(isDisplayed()));
        onView(withId(R.id.show_popwindow)).perform(click());

        // wait for 3s to show the disaster popupwindow
        Thread.sleep(3000);

        //find and close the DisasterInfo popupwindow
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));

        Thread.sleep(3000);

        onView(withId(R.id.show_taskdetails)).perform(click());
        Thread.sleep(3000);
    }

    @Test
    public void OfficerReportTest() throws InterruptedException {
        // Login Test
        onView(withId(R.id.username)).perform(typeText("aaa@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("aaaaaa"), closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);

        //ReportInfo popupwindow Test
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).perform(click());

        onView(withId(R.id.navigation_notifications)).perform(click());

        // check the layout and controls
        onView(withId(R.id.navigation_notifications)).perform(click());

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

        onView(ViewMatchers.withId(R.id.report_otherinfo))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_btn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_cancel))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(ViewMatchers.withId(R.id.report_submit))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the report container is displayed
        onView(withId(R.id.report_container));

        onView(withId(R.id.report_water)).perform(click());

        // Type the number of injured people
        onView(withId(R.id.report_injured)).perform(typeText("5"));

        onView(withId(R.id.report_otherinfo)).perform(typeText("Disaster has been controlled"));
        // Close the soft keyboard
        closeSoftKeyboard();
    }
}
