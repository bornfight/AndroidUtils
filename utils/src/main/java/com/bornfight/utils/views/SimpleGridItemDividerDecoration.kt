package com.bornfight.utils.views

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by tomislav on 20/10/2016.
 */

class SimpleGridItemDividerDecoration(context: Context, spacingDp: Float) : RecyclerView.ItemDecoration() {

    private var spacingPx: Int = 0

    init {
        this.spacingPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, spacingDp,
            context.resources.displayMetrics
        ).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.right = spacingPx / 2
        outRect.bottom = spacingPx / 2
        outRect.left = spacingPx / 2
        outRect.top = spacingPx / 2
    }
}
