package com.sparta.youandme.view.main.recyclerview.utility

import android.content.ClipData.Item
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sparta.youandme.R


abstract class SwipeToEditCallback(private val context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val editIcon = ContextCompat.getDrawable(context, R.drawable.icon_color_call)
    private val intrinsicWidth = editIcon!!.intrinsicWidth
    private val intrinsicHeight = editIcon!!.intrinsicHeight
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.left + dX,
                itemView.top.toFloat(),
                itemView.left.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }
        // Calculate position of edit icon
        val editIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val editIconLeft = itemView.left + itemHeight - intrinsicWidth
        val editIconRight = itemView.left + itemHeight
        val editIconBottom = editIconTop + intrinsicHeight

        // Draw the icon
        editIcon!!.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)
        editIcon.draw(c)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}