package com.example.testmc.activities

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.testmc.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun addConnection() {
        val connName = "New HiveMQ"
        val clientId = "12345"
        val brokerIp = "broker.hivemq.com"
        val port = 1883
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.connName)).perform(ViewActions.typeText(connName))
        onView(withId(R.id.clientID)).perform(ViewActions.typeText(clientId))
        onView(withId(R.id.brokerIP)).perform(ViewActions.typeText(brokerIp))
        onView(withId(R.id.portNum)).perform(ViewActions.typeText(port.toString()))
        onView(withId(R.id.saveConnBtn)).perform(click())
        onView(withText(connName)).check(matches(isDisplayed()))
    }

    @Test
    fun addTopicPublishAndSubscribe() {
        val connName = "rd_sensors"
        ActivityScenario.launch(MqttPanellActivityRv::class.java)
        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.topNameTxtLayout)).perform(ViewActions.typeText("rd_sensors"))
        onView(withId(R.id.radio_publish)).perform(click())
        onView(withId(R.id.saveTopicBtn)).perform(click())

        onView(withId(R.id.floatingActionButton)).perform(click())
        onView(withId(R.id.topNameTxtLayout)).perform(ViewActions.typeText("rd_sensors"))
        onView(withId(R.id.radio_subscribe)).perform(click())
        onView(withId(R.id.saveTopicBtn)).perform(click())
    }
    
    @Test
    fun recyclerViewTests() {
        ActivityScenario.launch(MainActivity::class.java)
        var recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.connRecyclerView)
        var itemCount = recyclerView.adapter?.itemCount

        if (itemCount != null) {
            onView(withId(R.id.connRecyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemCount.minus(1)))
        }

        onView(withId(R.id.connRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
    }
}