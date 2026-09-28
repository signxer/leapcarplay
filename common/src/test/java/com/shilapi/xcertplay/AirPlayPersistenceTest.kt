package com.shilapi.xcertplay

import com.shilapi.xcertplay.airplay.AirPlayDisplaySettings
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29], manifest = Config.NONE)
class AirPlayPersistenceTest {
    @Test
    fun newInstallDefaultsToSixtyFps() {
        val context = RuntimeEnvironment.getApplication()
        context.getSharedPreferences("xcertplay_airplay", 0).edit().clear().commit()

        assertEquals(AirPlayDisplaySettings.DEFAULT_FPS, AirPlayPersistence.loadFps(context))
        assertEquals(60, AirPlayPersistence.loadFps(context))
        assertEquals("Leapmotor", AirPlayPersistence.loadOemLabel(context))
    }
}
