package com.zima.myxkcdviewer.ui.fragments

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.zima.myxkcdviewer.data.di.ComicDatabaseModule
import com.zima.myxkcdviewer.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(ComicDatabaseModule::class)
class ComicNavigationViewFragmentTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun myClassMethod_ReturnsTrue() {
        activityRule.scenario.onActivity { }
    }

}