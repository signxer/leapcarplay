package com.shilapi.xcertplay

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class OutputSurfaceBindingTest {
    @Test
    fun replacingOrClearingSurfaceDetachesOldSurfaceBeforeAttachingNext() {
        val events = mutableListOf<String>()
        val binding = OutputSurfaceBinding<String>(
            detach = { events += "detach:$it" },
            attach = { events += "attach:$it" },
        )

        binding.set("cluster-a")
        binding.set("cluster-a")
        binding.set("cluster-b")
        binding.set(null)

        assertEquals(
            listOf("attach:cluster-a", "detach:cluster-a", "attach:cluster-b", "detach:cluster-b"),
            events,
        )
        assertEquals(null, binding.current)
    }

    @Test
    fun retainsSurfaceWhenSameInstanceIsReportedAgain() {
        val surface = Any()
        val binding = OutputSurfaceBinding<Any>(detach = {}, attach = {})

        binding.set(surface)
        binding.set(surface)

        assertSame(surface, binding.current)
    }
}
