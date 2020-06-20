package dev.jakal.pandemicwatch.presentation.common.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.jakal.pandemicwatch.R
import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.presentation.common.getThemeColor

abstract class SwipeDeleteCallback(
    private val context: Context
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_delete_24)
    private val backgroundDrawable = ColorDrawable(context.getThemeColor(R.attr.colorAccent))

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

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

        if (iconDrawable == null) return

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 100
        val iconMargin = (itemView.height - iconDrawable.intrinsicHeight) / 8
        val iconTop = itemView.top + (itemView.height - iconDrawable.intrinsicHeight) / 2
        val iconBottom = iconTop + iconDrawable.intrinsicHeight

        when {
            dX > 0 -> {
                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + iconDrawable.intrinsicWidth
                iconDrawable.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                backgroundDrawable.setBounds(
                    itemView.left - itemView.marginLeft,
                    itemView.top - itemView.marginTop,
                    itemView.left + dX.toInt() + backgroundCornerOffset - itemView.marginLeft,
                    itemView.bottom + itemView.marginBottom
                )
            }
            dX < 0 -> {
                val iconLeft = itemView.right - iconMargin - iconDrawable.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                iconDrawable.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                backgroundDrawable.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset + itemView.marginLeft,
                    itemView.top - itemView.marginTop,
                    itemView.right + itemView.marginRight,
                    itemView.bottom + itemView.marginBottom
                )
            }
            else -> {
                iconDrawable.setBounds(0, 0, 0, 0)
                backgroundDrawable.setBounds(0, 0, 0, 0)
            }
        }

        backgroundDrawable.draw(c)
        iconDrawable.draw(c)
    }
}

fun RecyclerView.setupSwipeDelete(
    adapter: ListAdapter<Country, CountryViewHolder>,
    onSwiped: (String) -> (Unit)
) {
    ItemTouchHelper(object : SwipeDeleteCallback(context) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val item = adapter.currentList[viewHolder.adapterPosition]
            val list = adapter.currentList.toMutableList()
                .apply { remove(item) }
            adapter.submitList(list)
            onSwiped.invoke(item.country)
        }
    }).attachToRecyclerView(this)
}