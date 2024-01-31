package com.peterchege.benchmark.util

import android.Manifest
import android.os.Build
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.UiObject2Condition
import androidx.test.uiautomator.Until
import com.peterchege.benchmark.util.HasChildrenOp.EXACTLY
import com.peterchege.benchmark.util.HasChildrenOp.AT_MOST
import com.peterchege.benchmark.util.HasChildrenOp.AT_LEAST


fun MacrobenchmarkScope.allowNotifications() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val command = "pm grant $packageName ${Manifest.permission.POST_NOTIFICATIONS}"
        device.executeShellCommand(command)
    }
}

fun UiDevice.waitAndFindObject(selector: BySelector, timeout: Long): UiObject2 {
    if (!wait(Until.hasObject(selector), timeout)) {
        throw AssertionError("Element not found on screen in ${timeout}ms (selector=$selector)")
    }

    return findObject(selector)
}

fun UiDevice.flingElementDownUp(element: UiObject2) {
    // Set some margin from the sides to prevent triggering system navigation
    element.setGestureMargin(displayWidth / 5)

    element.fling(Direction.DOWN)
    waitForIdle()
    element.fling(Direction.UP)
}


//fun untilHasChildren(
//    childCount: Int = 1,
//    op: HasChildrenOp = AT_LEAST,
//): UiObject2Condition<Boolean> = object : UiObject2Condition<Boolean>() {
//    override fun apply(element: UiObject2): Boolean = when (op) {
//        AT_LEAST -> element.childCount >= childCount
//        EXACTLY -> element.childCount == childCount
//        AT_MOST -> element.childCount <= childCount
//    }
//}

enum class HasChildrenOp {
    AT_LEAST,
    EXACTLY,
    AT_MOST,
}