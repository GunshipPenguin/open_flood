package com.gunshippenguin.openflood;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SettingsTest {

    private static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with "+childPosition+" child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }

    private static void clickColorButton(int index) {
        ViewInteraction colorButton = onView(
                allOf(withClassName(is("com.gunshippenguin.openflood.ColorButton")),
                        withParent(withId(R.id.buttonLayout)),
                        isDisplayed(),
                        nthChildOf(withId(R.id.buttonLayout), index)));
        colorButton.perform(click());
    }

    @Rule
    public ActivityTestRule<GameActivity> mActivityTestRule = new ActivityTestRule<>(GameActivity.class);

    @Test
    public void changeNumColorsTest() {
        // Click each of the 6 color buttons that should be initially present
        clickColorButton(0);
        clickColorButton(1);
        clickColorButton(2);
        clickColorButton(3);
        clickColorButton(4);
        clickColorButton(5);

        // Click the settings button to launch the SettingsActivity
        ViewInteraction settingsButton = onView(
                allOf(withId(R.id.settingsButton), isDisplayed()));
        settingsButton.perform(click());

        // Click the 8 color button
        ViewInteraction changeNumColorsRadioButton = onView(
                allOf(withText("8"), withParent(withId(R.id.numColorsRadioGroup)), isDisplayed()));
        changeNumColorsRadioButton.perform(click());

        // Apply the settings changes
        ViewInteraction applyButton = onView(
                allOf(withId(R.id.applyButton), withText("Apply"), isDisplayed()));
        applyButton.perform(click());

        // We should now have 8 color buttons, click them all
        clickColorButton(0);
        clickColorButton(1);
        clickColorButton(2);
        clickColorButton(3);
        clickColorButton(4);
        clickColorButton(5);
        clickColorButton(6);
        clickColorButton(7);

        // Go back to the SettingsActivity
        settingsButton.perform(click());

        // Enable color blind mode
        ViewInteraction colorBlindModeCheckBox = onView(
                allOf(withId(R.id.colorBlindCheckBox), isDisplayed()));
        colorBlindModeCheckBox.perform(click());

        // Enable the old color scheme
        ViewInteraction oldColorSchemeCheckBox = onView(
                allOf(withId(R.id.oldColorsCheckBox), isDisplayed()));
        oldColorSchemeCheckBox.perform(click());

        // Set the board size to 24x24
        ViewInteraction changeBoardSizeRadioButton = onView(
                allOf(withText("24x24"), withParent(withId(R.id.boardSizeRadioGroup)), isDisplayed()));
        changeBoardSizeRadioButton.perform(click());

        // Apply the new settings
        applyButton.perform(click());

        // Click all 8 of the color buttons again
        clickColorButton(0);
        clickColorButton(1);
        clickColorButton(2);
        clickColorButton(3);
        clickColorButton(4);
        clickColorButton(5);
        clickColorButton(6);
        clickColorButton(7);
    }

}
