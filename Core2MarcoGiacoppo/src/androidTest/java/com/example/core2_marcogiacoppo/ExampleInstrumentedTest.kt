package com.example.core2_marcogiacoppo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test

@LargeTest
@RunWith(AndroidJUnit4::class)
class Core2Test {
    //MainActivity
    private var title = 0
    private var nextButton = 0
    private var rentButton = 0

    //MainActivity2
    private var total = 0
    private var bar = 0
    private var saveButton = 0

    @Before
    fun initValidString() {
        //MainActivity
        title = R.id.title
        nextButton = R.id.next
        rentButton = R.id.rent

        //MainActivity2
        total = R.id.total
        bar = R.id.bar
        saveButton = R.id.save
    }

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testnNextButton() {
        val nextButton = onView(withId(nextButton))

        for (i in 1..3) {
            nextButton.perform(click())
        }

        val textView = onView(withId(title))
        textView.check(matches(withText("BMW S1000rr")))
    }

    @Test
    fun testSecondActivity() {
        val rentButton = onView(withId(rentButton))
        val nextButton = onView(withId(nextButton))

        for (i in 1..2) {
            nextButton.perform(click())
        }
        rentButton.perform(click())

        val textView = onView(withId(total))
        textView.check(matches(withText("$219")))
    }

    @Test
    fun testSeekBar() {
        val nextButton = onView(withId(nextButton))
        val rentButton = onView(withId(rentButton))

        nextButton.perform(click())
        rentButton.perform(click())

        val textView = onView(withId(total))
        textView.check(matches(withText("$149")))

        val totalRate = 7 * 149.0
        onView(withId(bar)).perform(swipeRight())
        textView.check(matches(withText("$$totalRate")))
    }

    @Test
    fun test0Days() {
        val nextButton = onView(withId(nextButton))
        val borrowButton = onView(withId(rentButton))

        nextButton.perform(click())
        borrowButton.perform(click())

        val textView = onView(withId(total))
        textView.check(matches(withText("$149")))

        onView(withId(bar)).perform(slowSwipeLeft())

        textView.check(matches(withText("$0.0")))
    }

    @Test
    fun testRented() {
        val nextButton = onView(withId(nextButton))
        val borrowButton = onView(withId(rentButton))
        val saveButton = onView(withId(saveButton))

        nextButton.perform(click())
        borrowButton.perform(click())

        onView(withId(bar)).perform(swipeRight())
        saveButton.perform(click())

        onView(withId(R.id.rent)).check(matches(Matchers.not(isEnabled())))

    }
}