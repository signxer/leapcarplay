package com.shilapi.xcertplay

internal data class PresentationDisplayCandidate(
    val displayId: Int,
    val name: String,
    val widthPixels: Int,
    val heightPixels: Int,
)

/** Prefer the vehicle's named HDMI2 output; only guess when there is one presentation display. */
internal object PresentationDisplaySelector {
    fun select(candidates: List<PresentationDisplayCandidate>): PresentationDisplayCandidate? =
        candidates.firstOrNull {
            it.name.contains("HDMI2", ignoreCase = true) || it.name.contains("HDMI 2", ignoreCase = true)
        } ?: candidates.singleOrNull()
}
