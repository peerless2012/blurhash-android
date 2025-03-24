package io.github.peerless2012.blurhash.impl

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import io.github.peerless2012.blurhash.decoder.BlurHashDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Blur hash use bitmap.
 */
class BitmapBlurHash(private val drawable: Drawable) : BlurHash {

    private val paint = Paint().also {
        it.flags = Paint.ANTI_ALIAS_FLAG
    }

    private var drawableBounds: Rect? = null

    private var blurBounds: Rect? = null

    private var blurBitmap: Bitmap? = null

    override fun setHash(hash: String?) {
        if (hash.isNullOrEmpty()) {
            blurBitmap = null
            return
        }
        GlobalScope.launch(Dispatchers.Main) {
            blurBitmap = decodeHash(hash)
            blurBitmap?.let {
                blurBounds = Rect(0, 0, it.width, it.height)
            }
            drawable.invalidateSelf()
        }
    }

    private suspend fun decodeHash(hash: String): Bitmap? {
        return withContext(Dispatchers.Default) {
            val pair = BlurHashDecoder.parse(hash)
            if (pair == null) {
                return@withContext null
            }
            val size = pair.first
            val colors = pair.second
            val bitmap = BlurHashDecoder.composeBitmap(size.width * 32,
                size.height * 32, size.width, size.height, colors, true)
            bitmap.prepareToDraw()
            return@withContext bitmap
        }
    }

    override fun onBoundsChange(bounds: Rect) {
        this.drawableBounds = bounds
    }

    override fun onDraw(canvas: Canvas) {
        if (blurBitmap == null) {
            return
        }
        canvas.drawBitmap(blurBitmap!!, blurBounds, drawableBounds!!, paint)
    }

}