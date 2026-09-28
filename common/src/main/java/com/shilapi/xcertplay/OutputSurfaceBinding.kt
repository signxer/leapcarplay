package com.shilapi.xcertplay

/** Owns one decoder output surface and guarantees detach-before-attach on replacement. */
internal class OutputSurfaceBinding<T : Any>(
    private val detach: (T) -> Unit,
    private val attach: (T) -> Unit,
) {
    var current: T? = null
        private set

    fun set(next: T?) {
        if (current === next) return
        current?.let(detach)
        current = next
        next?.let(attach)
    }
}
