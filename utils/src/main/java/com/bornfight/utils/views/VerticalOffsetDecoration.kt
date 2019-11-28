package com.bornfight.utils.views

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by tomislav on 07/03/2017.
 */

class VerticalOffsetDecoration constructor(
    context: Context,
    offset: Float,
    private val spanCount: Int
) : RecyclerView.ItemDecoration() {

    @Deprecated("Please specify span count")
    constructor(context: Context, offset: Float) : this(context, offset, 1)

    private val offset: Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, offset, context.resources.displayMetrics).toInt()

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition < spanCount) {
            outRect.top = 0
        } else {
            outRect.top = offset / 2
        }

        if ((parent.adapter?.itemCount ?: 0) - parent.getChildAdapterPosition(view) < spanCount) {
            outRect.bottom = 0
        } else {
            outRect.bottom = offset / 2
        }
    }
}