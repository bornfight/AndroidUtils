package com.bornfight.utils

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView

/**
 * Created by tomislav on 10/05/2017.
 */

object ImageViewUtil {

    fun setBlackWhite(imageView: ImageView) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)

        val filter = ColorMatrixColorFilter(matrix)
        imageView.colorFilter = filter
    }

}
