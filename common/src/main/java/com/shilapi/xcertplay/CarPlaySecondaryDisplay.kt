package com.shilapi.xcertplay

import android.app.Presentation
import android.content.Context
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.view.Display
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout

/** Full-screen target for CarPlay's alternate/cluster video stream. */
internal class CarPlaySecondaryDisplay(
    context: Context,
    display: Display,
    private val onSurfaceChanged: (Surface?) -> Unit,
) : Presentation(context, display) {
    private var surface: Surface? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )
        val texture = TextureView(context).apply {
            isOpaque = true
            setBackgroundColor(Color.BLACK)
            surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
                    surface?.release()
                    Surface(texture).also {
                        surface = it
                        onSurfaceChanged(it)
                    }
                }

                override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) = Unit

                override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
                    onSurfaceChanged(null)
                    surface?.release()
                    surface = null
                    return true
                }

                override fun onSurfaceTextureUpdated(texture: SurfaceTexture) = Unit
            }
        }
        setContentView(texture, FrameLayout.LayoutParams(-1, -1))
        window?.decorView?.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
    }

    override fun dismiss() {
        onSurfaceChanged(null)
        super.dismiss()
    }
}
