package io.github.peerless2012.blurhash.impl

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import io.github.peerless2012.blurhash.decoder.BlurHashDecoder
import org.intellij.lang.annotations.Language

/**
 * Blur hash use shader.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class ShaderBlurHash : BlurHash {

    private val BLUR_HASH_MAX_SIZE = 32

    @Language("AGSL")
    val blurHashAGSL: String = """
        uniform vec2 startPos;
        uniform vec2 iResolution;
        uniform ivec2 num;
        uniform vec4 colors[32];
        float linearTosRGB(float value) {
            float v = max(0, min(1, value));
            if (v <= 0.0031308) {
                return v * 12.92;
            } else {
                return pow(v, 1.0 / 2.4) * 1.055 - 0.055;
            }
        }
        vec4 main(float2 fragCoord) {
            vec2 uv = (fragCoord.xy - startPos.xy) / iResolution.xy;
            vec3 color = vec3(0.0);
            vec2 uvpi = uv * 3.14159265358979323846;
            int size = num.x * num.y;
            for (int index = 0; index < 32; index++) {
                if (index >= size) break;
                vec3 sColor = colors[index].rgb;
                int row = index / num.x;  // 直接整数除法
                int col = index - row * num.x;  // 直接取模运算
                vec2 loopPos = vec2(float(col), float(row));
                vec2 basics = uvpi * loopPos;
                color += sColor * cos(basics.x) * cos(basics.y);
            }
            return vec4(linearTosRGB(color.r), linearTosRGB(color.g), linearTosRGB(color.b), 1.0);
}
    """.trimIndent()

    private val paint = Paint().also {
        it.flags = Paint.ANTI_ALIAS_FLAG
    }

    private val shader = RuntimeShader(blurHashAGSL)

    private var validateShader: Boolean = false

    constructor() {
        paint.shader = shader
    }

    override fun setHash(hash: String?) {
        val pair = BlurHashDecoder.parse(hash)
        if (pair == null) {
            validateShader = false
            return
        }
        val size = pair.first
        if (size.width * size.height > BLUR_HASH_MAX_SIZE) {
            validateShader = false
            return
        }
        val colors = FloatArray(BLUR_HASH_MAX_SIZE * 4)
        var index = 0
        pair.second.forEach {
            System.arraycopy(it, 0, colors, index, it.size)
            index += it.size
            colors[index] = 0f
            index++
        }
        shader.setFloatUniform("colors", colors)
        shader.setIntUniform("num", size.width, size.height)
        validateShader = true
    }

    override fun onBoundsChange(bounds: Rect) {
        shader.setFloatUniform("startPos", 0f, 0f)
        shader.setFloatUniform("iResolution", bounds.width().toFloat(), bounds.height().toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        if (!validateShader) return
        canvas.drawPaint(paint)
    }

}