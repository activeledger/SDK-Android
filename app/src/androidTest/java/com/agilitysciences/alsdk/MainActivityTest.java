package com.agilitysciences.alsdk;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.activeledgersdk.ActiveLedgerSDK;
import com.example.activeledgersdk.utility.Utility;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void listGoesOverTheFold() {
        onView(withText("Activeledger")).check(matches(isDisplayed()));

        onView(withId(R.id.generate_keys))        // withId(R.id.my_view) is a ViewMatcher
                .perform(click())               // click() is a ViewAction
                .check(matches(isDisplayed())); // matches(isDisplayed()) is a ViewAssertion

    }


    @Test
    public void clickGenerateKeys_populatePublicKey(){
        onView(withId(R.id.pubkey)).check(matches(withText("")));
        onView(withText("Generate keys")).perform(click());
        try {
            onView(withId(R.id.pubkey)).check(matches(withText(ActiveLedgerSDK.readFileAsString(Utility.PUBLICKEY_FILE))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void clickGenerateKeys_populatePrivateKey(){
        onView(withId(R.id.prikey)).check(matches(withText("")));
        onView(withText("Generate keys")).perform(click());
        try {
            onView(withId(R.id.prikey)).check(matches(withText(ActiveLedgerSDK.readFileAsString(Utility.PRIVATEKEY_FILE))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void changeSpinnerValue(){
        onView(withId(R.id.keytype_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Elliptic Curve"))).perform(click());
        onView(withId(R.id.keytype_spinner)).check(matches(withSpinnerText(containsString("Elliptic Curve"))));
    }


    @Test
    public void clickOnboardKeys_validResponse(){
        onView(withText("Generate keys")).perform(click());

        onView(withId(R.id.keyname_et)).check(matches(withText("")));

        onView(withId(R.id.keyname_et)).perform(scrollTo(), click());

        onView(withId(R.id.keyname_et)).perform(clearText(), typeText("keyName")).perform(closeSoftKeyboard());

        onView(withId(R.id.keyname_et)).check(matches(not(withText(""))));
        onView(withId(R.id.btn_onboard)).perform(click());

        onView(withId(R.id.onBoardId_tv)).perform(scrollTo()).check(matches(not(withText(""))));
        onView(withId(R.id.onBoardName_tv)).perform(scrollTo()).check(matches(not(withText(""))));

    }


    @Test
    public void changeSpinnerValue_generateECKey_validResponse(){
        onView(withId(R.id.keytype_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Elliptic Curve"))).perform(click());
        onView(withId(R.id.keytype_spinner)).check(matches(withSpinnerText(containsString("Elliptic Curve"))));

        onView(withText("Generate keys")).perform(click());

        try {
            onView(withId(R.id.prikey)).check(matches(withText(ActiveLedgerSDK.readFileAsString(Utility.PRIVATEKEY_FILE))));
            onView(withId(R.id.pubkey)).check(matches(withText(ActiveLedgerSDK.readFileAsString(Utility.PUBLICKEY_FILE))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.keyname_et)).check(matches(withText("")));

        onView(withId(R.id.keyname_et)).perform(scrollTo(), click());

        onView(withId(R.id.keyname_et)).perform(clearText(), typeText("keyName")).perform(closeSoftKeyboard());

        onView(withId(R.id.keyname_et)).check(matches(not(withText(""))));
        onView(withId(R.id.btn_onboard)).perform(click());

        onView(withId(R.id.onBoardId_tv)).perform(scrollTo()).check(matches(not(withText(""))));
        onView(withId(R.id.onBoardName_tv)).perform(scrollTo()).check(matches(not(withText(""))));

    }



}