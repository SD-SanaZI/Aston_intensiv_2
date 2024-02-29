package com.example.aston_intensiv_2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import android.widget.ImageView
import androidx.annotation.IdRes
import kotlin.random.Random


private const val PROGRESS_ANIMATE_DURATION = 1000L

class CustomButton(
    context: Context,
    attr: AttributeSet,
) : LinearLayout(context, attr) {
    private val picture by find<ImageView>(R.id.imageView)
    var degree: Float = 0f

    init {
        inflate(context, R.layout.custom_button, this)
    }

    fun animationStart() {
        picture.rotation = 0f
        val newDegree = degree + Random.nextFloat() * 1080
        val animation = createAnimation(degree, newDegree)
        picture.startAnimation(animation)
        degree = newDegree % 360
    }

    private fun createAnimation(fromDegree: Float, toDegree: Float): Animation {
        val animation = RotateAnimation(fromDegree, toDegree, picture.pivotX, picture.pivotY)
        animation.duration =
            PROGRESS_ANIMATE_DURATION + toDegree.toLong() / 360 * PROGRESS_ANIMATE_DURATION
        animation.fillAfter = true
        return animation
    }

    fun updateRotation(){
        picture.rotation = degree
    }
}

fun <T : View> View.find(@IdRes idRes: Int): Lazy<T> {
    return lazy { findViewById(idRes) }
}