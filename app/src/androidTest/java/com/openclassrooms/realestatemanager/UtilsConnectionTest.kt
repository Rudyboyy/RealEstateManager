package com.openclassrooms.realestatemanager


import android.content.Context
import android.net.ConnectivityManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UtilsConnectionTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )

    @Test
    fun testConnectivity() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val isConnected = Utils.isInternetAvailable(context)

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withText(R.string.map)).perform(click())

        if (isConnected) {
            onView(withId(R.id.map)).check(matches(isDisplayed()))
        } else {
            onView(withId(R.id.no_internet_image)).check(matches(isDisplayed()))
        }
    }
}