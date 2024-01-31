package com.peterchege.benchmark.baselineprofiles

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import com.peterchege.benchmark.util.flingElementDownUp
import com.peterchege.benchmark.util.waitAndFindObject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HomeBaselineProfileGenerator {


    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateHomeScreenProfile() {
        baselineProfileRule.collect(
            packageName = "com.peterchege.blogger",
            includeInStartupProfile = false,
        ) {
            startActivityAndWait()
            waitForLoadingToFinish()
            scrollHomeScreen()

        }
    }
}


fun MacrobenchmarkScope.scrollHomeScreen(){
    println("before scroll")
    val feedList = device.findObject(By.res("feed"))
    println("found scroll")
    device.flingElementDownUp(feedList)
    println("scrolling")
}

fun MacrobenchmarkScope.waitForLoadingToFinish() {
    println("Finding loading indicator")
    // Wait until content is loaded by checking if topics are loaded
    device.wait(Until.gone(By.res("loadingIndicator")), 5_000)
    println("Found indicator")
    // Sometimes, the loading wheel is gone, but the content is not loaded yet
    // So we'll wait here for topics to be sure
    val obj = device.waitAndFindObject(By.res("feed"), 10_000)
    println("Found feed")
}