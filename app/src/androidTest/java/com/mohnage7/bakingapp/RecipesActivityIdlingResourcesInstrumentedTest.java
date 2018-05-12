package com.mohnage7.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mohnage7.bakingapp.recipes.view.RecipesActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipesActivityIdlingResourcesInstrumentedTest {
    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityActivityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }


    @Test
    public void scrollToSpecificItem_checkLaunchedActivity() {
        // check if recycler is displayed
        onView(withId(R.id.movies_recycler_view)).check(matches(isDisplayed()));
        // check that recycler actually has data
        onView(withId(R.id.movies_recycler_view)).check(ViewAssertions.matches(isDisplayed()));
        // perform click on it's first item
        onView(withId(R.id.movies_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        // check if next activity is launched
        onView(withId(R.id.item_ingredients_list)).check(matches(isDisplayed()));
    }

    // unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
