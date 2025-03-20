package io.github.peerless2012.blurhash.impl

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import io.github.peerless2012.blurhash.decoder.BlurHashDecoder

/**
 * Blur hash use bitmap.
 */
class BitmapBlurHash : BlurHash {

    private val paint = Paint().also {
        it.flags = Paint.ANTI_ALIAS_FLAG
    }

    private var bounds: Rect? = null

    private var hash: String? = null

    private var bitmap: Bitmap? = null

    private fun updateBitmap() {
        if (hash == null || bounds == null) {
            bitmap = null
            return
        }
        // We may not need the view size bitmap.
        bitmap = BlurHashDecoder.decode(hash, bounds!!.width(), bounds!!.height())
    }

    override fun setHash(hash: String?) {
        this.hash = hash
        updateBitmap()
    }

    override fun onBoundsChange(bounds: Rect) {
        this.bounds = bounds
        updateBitmap()
    }

    override fun onDraw(canvas: Canvas) {
        if (bitmap == null) {
            return
        }
        canvas.drawBitmap(bitmap!!, bounds!!, bounds!!, paint)
    }

}