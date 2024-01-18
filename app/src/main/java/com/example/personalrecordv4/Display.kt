package com.example.personalrecordv4

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


class Display(context: Context?, @Nullable attrs: AttributeSet?) :
    View(context, attrs) {
    var srcRect = Rect(0, 0, 480, 640)
    var disRect: Rect? = null
    var b: Bitmap? = null
    fun getBitmap(bitmap: Bitmap?) {
        b = bitmap
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        invalidate()
        disRect = Rect(0, 0, getRight(), getBottom())
        if (b != null) {
            canvas.drawBitmap(b!!, srcRect, disRect!!, null)
        }
    }
}