package com.example.aston_intensiv_2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class CustomPicture(
    context: Context,
    attr: AttributeSet,
) : View(context, attr) {
    var color: Rainbow = Rainbow.NONE
    private val paint: Paint = Paint()
    private val bitmapAndUrlList: MutableList<BitmapAndUrl> = mutableListOf()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (color) {
            Rainbow.RED, Rainbow.YELLOW, Rainbow.BLUE, Rainbow.VIOLET -> drawText(canvas)
            Rainbow.ORANGE, Rainbow.GREEN, Rainbow.INDIGO -> drawPicture(canvas)
            Rainbow.NONE -> drawNone(canvas)
        }
    }

    private fun drawPicture(canvas: Canvas) {
        val urlString = createUrlString(color.getColor())
        val bmp = bitmapAndUrlList.find { it.url == urlString }?.bmp
        if (bmp == null) {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                addedBitmapToList(urlString)
                invalidate()
            }
        } else {
            canvas.drawBitmap(bmp, Matrix(), null)
        }
    }

    private fun addedBitmapToList(urlString: String): Bitmap {
        val url = URL(urlString)
        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        bitmapAndUrlList.add(
            BitmapAndUrl(
                urlString,
                bmp ?: Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            )
        )
        return bmp
    }

    private fun drawText(canvas: Canvas) {
        canvas.drawColor(Color.GRAY)
        paint.color = color.getColor()
        paint.textSize = 160.0f
        canvas.drawText("COLOR", 200f, 200f, paint)
    }

    private fun drawNone(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
    }

    fun newColor(newColor: Rainbow) {
        color = newColor
    }
}

fun createUrlString(color1: Int): String {
    var str = Integer.toHexString(color1)
    if (str.length > 6) str = str.substring(str.length - 6)
    if (str.length < 6) str = str.padStart(6, '0')
    return "https://dummyimage.com/1000x1000/$str/000.png&text=Picture"
}

enum class Rainbow(val number: Int?) {
    RED(3) {
        override fun getColor() = Color.rgb(0xFF, 0x00, 0x00)
    },
    ORANGE(2) {
        override fun getColor() = Color.rgb(0xFF, 0xA4, 0x4D)
    },
    YELLOW(1) {
        override fun getColor() = Color.rgb(0xFF, 0xC8, 0x4B)
    },
    GREEN(0) {
        override fun getColor() = Color.rgb(0x96, 0xFF, 0x96)
    },
    BLUE(6) {
        override fun getColor() = Color.rgb(0x7D, 0xFF, 0xFF)
    },
    INDIGO(5) {
        override fun getColor() = Color.rgb(0x00, 0x96, 0xFF)
    },
    VIOLET(4) {
        override fun getColor() = Color.rgb(0xAF, 0x96, 0xF5)
    },
    NONE(null) {
        override fun getColor() = Color.rgb(0xFF, 0xFF, 0xFF)
    };

    abstract fun getColor(): Int
}

data class BitmapAndUrl(
    val url: String,
    val bmp: Bitmap
)