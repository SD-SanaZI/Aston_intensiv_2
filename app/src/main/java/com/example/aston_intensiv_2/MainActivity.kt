package com.example.aston_intensiv_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

const val KEY_DEGREE = "key degree"
const val KEY_COLOR = "key_color"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val slotMachine = findViewById<CustomButton>(R.id.slot_machine)
        val rollButton = slotMachine.find<Button>(R.id.main_button)
        val picture = findViewById<CustomPicture>(R.id.picture)
        val zeroingButton = findViewById<Button>(R.id.zeroing)

        if (savedInstanceState != null) {
            val degree = savedInstanceState.getFloat(KEY_DEGREE)
            slotMachine.degree = degree
            slotMachine.updateRotation()
            var part: Int? = savedInstanceState.getInt(KEY_COLOR)
            if (part == -1) part = null
            val rainbow = getRainbowFromPart(part)
            picture.newColor(rainbow)
            picture.invalidate()
        }
        rollButton.value.setOnClickListener {
            slotMachine.animationStart()
            val rainbow = getRainbowFromDegree(slotMachine.degree)
            picture.newColor(rainbow)
            picture.invalidate()
        }
        zeroingButton.setOnClickListener {
            picture.newColor(Rainbow.NONE)
            picture.invalidate()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val slotMachine = findViewById<CustomButton>(R.id.slot_machine)
        val picture = findViewById<CustomPicture>(R.id.picture)
        outState.putFloat(KEY_DEGREE, slotMachine.degree)
        outState.putInt(KEY_COLOR, picture.color.number ?: -1)
    }

    private fun getRainbowFromDegree(degree: Float): Rainbow {
        val part = degree * 7 / 360
        return getRainbowFromPart(part.toInt())
    }

    private fun getRainbowFromPart(part: Int?): Rainbow {
        val rainbow = Rainbow.entries.find { it.number?.equals(part) ?: false }
        return rainbow ?: Rainbow.NONE
    }
}