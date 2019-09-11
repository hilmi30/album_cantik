package com.kartinimedia.albumcantik.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoratorGrid(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {

        with(outRect) {

            top = spaceHeight
            bottom = spaceHeight

            if (parent.getChildAdapterPosition(view) % 2 == 0) {
                left = spaceHeight
                right = spaceHeight.div(2)
            } else {
                left = spaceHeight.div(2)
                right = spaceHeight
            }
        }
    }
}