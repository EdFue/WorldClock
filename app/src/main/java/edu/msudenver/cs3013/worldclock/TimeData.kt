package edu.msudenver.cs3013.worldclock

data class TimeData(
    val timeZoneId: String,
    val timeZoneName: String,
    val dstOffset: Long, // Daylight Saving Time offset
    val rawOffset: Long  // Raw offset from UTC
)

