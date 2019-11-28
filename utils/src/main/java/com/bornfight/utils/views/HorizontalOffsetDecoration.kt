package com.bornfight.utils.views

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by tomislav on 07/03/2017.
 */

class HorizontalOffsetDecoration(context: Context, offset: Float) : RecyclerView.ItemDecoration() {

    private val offset: Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, offset, context.resources.displayMetrics).toInt()

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)

        if (itemPosition == 0) {
            outRect.left = 0
        } else {
            outRect.left = offset / 2
        }

        if (itemPosition == (parent.adapter?.itemCount ?: 0) - 1) {
            outRect.right = 0
        } else {
            outRect.right = offset / 2
        }
    }
}