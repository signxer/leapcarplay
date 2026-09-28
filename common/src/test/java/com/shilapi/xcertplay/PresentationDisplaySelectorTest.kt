package com.shilapi.xcertplay

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PresentationDisplaySelectorTest {
    private val main = PresentationDisplayCandidate(0, "Built-in screen", 1920, 1080)

    @Test
    fun selectsHdmi2ByNameEvenWhenOtherPresentationDisplaysExist() {
        val hdmi2 = PresentationDisplayCandidate(3, "HDMI2", 720, 720)
        val hdmi1 = PresentationDisplayCandidate(2, "HDMI1", 1280, 720)

        assertEquals(hdmi2, PresentationDisplaySelector.select(listOf(main, hdmi1, hdmi2)))
    }

    @Test
    fun acceptsOnlyUnambiguousUnnamedFallback() {
        assertEquals(main, PresentationDisplaySelector.select(listOf(main)))
        assertNull(PresentationDisplaySelector.select(listOf(main, PresentationDisplayCandidate(1, "External", 800, 600))))
    }
}
