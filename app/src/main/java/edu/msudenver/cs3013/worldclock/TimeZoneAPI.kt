package edu.msudenver.cs3013.worldclock

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TimeZoneAPI {
    @GET("maps/api/timezone/json")
    suspend fun getTimeZone(
        @Query("location") location: String,
        @Query("timestamp") timestamp: Long,
        @Query("key") key: String // Add this line
    ): Response<TimeData>
}


