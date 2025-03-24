package io.github.peerless2012.blurhash

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import io.github.peerless2012.blurhash.R
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity () {

    private val imageArray = intArrayOf(
        R.drawable.origin_a,
        R.drawable.origin_b,
        R.drawable.origin_c,
        R.drawable.origin_d,
    )

    private val blurArray = arrayOf(
        "LEHLh[WB2yk8pyoJadR*.7kCMdnj",
        "LGF5?xYk^6#M@-5c,1J5@[or[Q6.",
        "L6PZfSi_.AyE_3t7t7R**0o#DgR4",
        "LKN]Rv%2Tw=w]~RBVZRi};RPxuwH"
    )

    private var index = 0

    private lateinit var spinner: Spinner

    private lateinit var checkBox: CheckBox

    private lateinit var originImage: ImageView

    private lateinit var hashText: TextView

    private lateinit var blurImage: ImageView

    private lateinit var drawable: BlurHashDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinner = findViewById(R.id.main_spinner)
        val items = resources.getStringArray(R.array.spinner_items)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        spinner.adapter = adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                updateContent(position)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }

        }
        checkBox = findViewById(R.id.main_mode)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            drawable = BlurHashDrawable(isChecked)
            blurImage.setImageDrawable(drawable)
            updateContent(index)
        }
        originImage = findViewById(R.id.main_origin)
        hashText = findViewById(R.id.main_hash)
        blurImage = findViewById(R.id.main_blur)
        drawable = BlurHashDrawable(false)
        blurImage.setImageDrawable(drawable)
    }

    private fun updateContent(index: Int) {
        this.index = index
        originImage.setImageResource(imageArray[index])
        hashText.text = blurArray[index]
        drawable.setBlurHash(blurArray[index])
    }

}