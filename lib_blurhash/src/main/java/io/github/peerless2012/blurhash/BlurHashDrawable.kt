package io.github.peerless2012.blurhash

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import io.github.peerless2012.blurhash.impl.BitmapBlurHash
import io.github.peerless2012.blurhash.impl.BlurHash
import io.github.peerless2012.blurhash.impl.ShaderBlurHash

class BlurHashDrawable : Drawable {

    private val blurHash: BlurHash

    constructor(): this(true)

    constructor(shader: Boolean): super() {
        blurHash = if (shader && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ShaderBlurHash()
        } else {
            BitmapBlurHash()
        }
    }

    public fun setBlurHash(hash: String?) {
        blurHash.setHash(hash)
        invalidateSelf()
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        blurHash.onBoundsChange(bounds)
    }

    override fun draw(canvas: Canvas) {
        blurHash.onDraw(canvas)
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(filter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }


}