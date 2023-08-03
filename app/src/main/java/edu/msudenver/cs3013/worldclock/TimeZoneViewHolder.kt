package edu.msudenver.cs3013.worldclock

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TimeZoneViewHolder(
    containerView: View
) : RecyclerView.ViewHolder(containerView) {
    private val timezoneView: TextView
            by lazy { containerView.findViewById(R.id.item_city) }
    private val timeView: TextView
            by lazy { containerView.findViewById(R.id.item_time) }

    fun bindData(timeData: TimeData) {
        val timestamp = System.currentTimeMillis() / 1000L // Current time in seconds
        Log.d(
            "TimeZoneViewHolder",
            "Timestamp: $timestamp, dstOffset: ${timeData.dstOffset}, rawOffset: ${timeData.rawOffset}"
        )
        timezoneView.text = timeData.timeZoneId

        // Calculate the total epoch time considering the dstOffset and rawOffset, all in seconds
        val epoch = timestamp + timeData.dstOffset + timeData.rawOffset

        val date = Date(epoch * 1000) // Convert to milliseconds for Date constructor

        // Format the date using the desired pattern
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeFormat.timeZone = TimeZone.getTimeZone("UTC")

        val time = timeFormat.format(date)
        timeView.text = time
    }
}
