package edu.msudenver.cs3013.worldclock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class TimeZoneAdapter(
    private val layoutInflater: LayoutInflater,
    private val timeZones: MutableList<TimeData>

) : RecyclerView.Adapter<TimeZoneViewHolder>() {
    val swipeToDeleteCallback = SwipeToDeleteCallback()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeZoneViewHolder {
        val view = layoutInflater.inflate(R.layout.item_world_clock, parent, false)
        return TimeZoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeZoneViewHolder, position: Int) {
        holder.bindData(timeZones[position])
    }

    override fun getItemCount(): Int = timeZones.size

    fun addTimeZones(timeZones: List<TimeData>) {
        this.timeZones.addAll(timeZones)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        timeZones.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clearTimeZones() {
        this.timeZones.clear()
        notifyDataSetChanged()
    }

    inner class SwipeToDeleteCallback :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) = if (viewHolder is TimeZoneViewHolder) {
            makeMovementFlags(
                ItemTouchHelper.ACTION_STATE_IDLE,
                ItemTouchHelper.RIGHT
            ) or makeMovementFlags(
                ItemTouchHelper.ACTION_STATE_SWIPE,
                ItemTouchHelper.RIGHT
            )
        } else {
            0
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            removeItem(position)
        }
    }
}