package com.shilapi.xcertplay.media

import android.view.Surface
import com.shilapi.xcertplay.airplay.VideoCodec
import java.util.ArrayDeque
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.TimeUnit
import kotlin.concurrent.withLock

internal sealed interface VideoJob {
    data class Config(val codec: VideoCodec, val codecData: ByteArray) : VideoJob
    data class Frame(val nalus: ByteArray, val receivedNs: Long = System.nanoTime()) : VideoJob
    data class SurfaceChanged(val surface: Surface?) : VideoJob
    data object Resync : VideoJob
}

/** Do not resume dependent pictures after losing a reference frame. */
internal class VideoReferenceChain {
    var needsKeyFrame = true
        private set
    fun reset() { needsKeyFrame = true }
    fun accepts(bytes: ByteArray, codec: VideoCodec): Boolean =
        !needsKeyFrame || MediaCodecSupport.isRandomAccess(bytes, codec)
    fun onQueued() { needsKeyFrame = false }
}

/** Limit latency and memory without ever dropping a reference frame silently. */
internal class VideoDecodeQueue(
    // Wi-Fi delivers frames in bursts after a radio gap; the decoder's 250 ms age check bounds latency.
    private val maxFrames: Int = 60,
    private val maxBytes: Int = 8 * 1024 * 1024,
) {
    private val lock = ReentrantLock()
    private val available = lock.newCondition()
    private val jobs = ArrayDeque<VideoJob>()
    private var queuedFrameCount = 0
    private var queuedFrameBytes = 0L

    fun offer(job: VideoJob) {
        lock.withLock {
            if (job is VideoJob.Frame) {
                if (queuedFrameCount >= maxFrames || queuedFrameBytes + job.nalus.size > maxBytes) {
                    discardFramesLocked()
                    jobs.addLast(VideoJob.Resync)
                }
                // A single oversized frame is also a lost reference chain.
                if (job.nalus.size > maxBytes) {
                    available.signal()
                    return
                }
                queuedFrameCount++
                queuedFrameBytes += job.nalus.size
            }
            jobs.addLast(job)
            available.signal()
        }
    }

    fun discardFrames() {
        lock.withLock { discardFramesLocked() }
    }

    @Throws(InterruptedException::class)
    fun poll(timeoutMillis: Long): VideoJob? = lock.withLock {
        var remainingNanos = TimeUnit.MILLISECONDS.toNanos(timeoutMillis.coerceAtLeast(0))
        while (jobs.isEmpty() && remainingNanos > 0) {
            remainingNanos = available.awaitNanos(remainingNanos)
        }
        if (jobs.isEmpty()) return null
        jobs.removeFirst().also { job ->
            if (job is VideoJob.Frame) {
                queuedFrameCount--
                queuedFrameBytes -= job.nalus.size
            }
        }
    }

    private fun discardFramesLocked() {
        val iterator = jobs.iterator()
        while (iterator.hasNext()) {
            when (val job = iterator.next()) {
                is VideoJob.Frame -> {
                    iterator.remove()
                    queuedFrameCount--
                    queuedFrameBytes -= job.nalus.size
                }
                VideoJob.Resync -> iterator.remove()
                else -> Unit
            }
        }
    }
}

/** Drain output while waiting for input: full output buffers can otherwise starve input forever. */
internal object VideoInputPump {
    fun acquire(
        running: () -> Boolean,
        drain: () -> Unit,
        dequeue: () -> Int,
        nanoTime: () -> Long = System::nanoTime,
        timeoutNs: Long = TimeUnit.MILLISECONDS.toNanos(500),
    ): Int {
        val start = nanoTime()
        while (running()) {
            drain()
            val index = dequeue()
            if (index >= 0) return index
            if (nanoTime() - start >= timeoutNs) break
        }
        return -1
    }
}
