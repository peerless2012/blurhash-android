package io.github.peerless2012.blurhash.impl

import android.graphics.Canvas
import android.graphics.Rect

interface BlurHash {

    fun setHash(hash: String?)

    fun onBoundsChange(bounds: Rect)

    fun onDraw(canvas: Canvas)

}