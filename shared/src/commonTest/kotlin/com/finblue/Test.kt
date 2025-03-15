package com.finblue

import kotlin.test.Test
import kotlin.test.assertTrue

class CommonGreetingTest {

    @Test
    fun testBooleanCondition() {
        val isKotlinAwesome = true
        assertTrue(isKotlinAwesome, "Kotlin should be awesome")
    }
}