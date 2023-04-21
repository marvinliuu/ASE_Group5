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

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.testdisasterevent.ui.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;

public class Apptest_Citizen {
    @Rule
    public ActivityTestRule<LoginActivity> loginActivityTestRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void CitizenHomeTest() throws InterruptedException {
        // Login Test
        onView(withId(R.id.username)).perform(typeText("1@1.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("111111"), closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);

        // BusStopInfo Test
        onView(withId(R.id.get_busInfo)).perform(click());

        // Reroute Function Test
        onView(withId(R.id.start_location)).perform(click());
        onView(withId(R.id.start_location)).perform(typeText("buswells"));
        closeSoftKeyboard();
        onView(withId(R.id.des_location)).perform(click());
        onView(withId(R.id.des_location)).perform(typeText("tara street"));
        closeSoftKeyboard();
        onView(withId(R.id.enter_btn)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));

    }

    @Test
    public void CitizenAccountTest() throws InterruptedException {
        // Login Test
        onView(withId(R.id.username)).perform(typeText("1@1.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("111111"), closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);


        //colse the DisasterInfo popupwindow
        onView(withId(R.id.navigation_account)).perform(click());

        // check the information details
        onView(withId(R.id.account_login_info)).check(matches(isDisplayed()));
        onView(withId(R.id.account_type_text)).check(matches(withText("Citizen")));
        onView(withId(R.id.account_name_info)).check(matches(withText("cai")));
        onView(withId(R.id.account_email_info)).check(matches(withText("1@1.com")));
        onView(withId(R.id.account_mobile_info)).check(matches(withText("123456")));
    }

    @Test
    public void CitizenDisasterTest() throws InterruptedException {
        // Login Test
        onView(withId(R.id.username)).perform(typeText("1@1.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("111111"), closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);

        //Jump to Disaster page
        onView(withId(R.id.navigation_dashboard)).perform(click());

        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);

        onView(withId(R.id.close_btn)).perform(click());
        // check the information details
        onView(withId(R.id.show_popwindow)).perform(click());
        onView(withId(R.id.tv_pop_name)).check(matches(isDisplayed()));
        onView(withId(R.id.close_btn)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void CitizenReportTest() throws InterruptedException {
        // Login Test
        onView(withId(R.id.username)).perform(typeText("1@1.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("111111"), closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        // Check that the next activity is launched
        onView(withId(R.id.container)).check(matches(isDisplayed()));

        // wait for 3 seconds until PopupWindow shows
        Thread.sleep(3000);


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

        onView(withId(R.id.report_water)).perform(click());

        // Type the number of injured people
        onView(withId(R.id.report_injured)).perform(typeText("5"));

        // Close the soft keyboard
        closeSoftKeyboard();
    }
}
