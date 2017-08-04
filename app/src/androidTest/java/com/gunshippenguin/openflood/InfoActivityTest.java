package com.gunshippenguin.openflood;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class InfoActivityTest {

    @Rule
    public ActivityTestRule<GameActivity> mActivityTestRule = new ActivityTestRule<>(GameActivity.class);

    @Test
    public void infoActivityTest() {
        // Launch the Info Activity
        ViewInteraction infoActivityButton = onView(
                allOf(withId(R.id.infoButton), isDisplayed()));
        infoActivityButton.perform(click());

        // Click the back button
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.backButton), withText("Back")));
        appCompatButton.perform(scrollTo(), click());

        // Verify that we are now back in the GameActivity by checking that the FloodView is displayed
        ViewInteraction view = onView(
                allOf(withId(R.id.floodView)));
        view.check(matches(isDisplayed()));
    }
}
