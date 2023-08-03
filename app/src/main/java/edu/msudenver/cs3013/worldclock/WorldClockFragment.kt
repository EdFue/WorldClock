package edu.msudenver.cs3013.worldclock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class WorldClockFragment : Fragment() {

    private lateinit var timeZoneAdapter: TimeZoneAdapter
    private val timeZones = mutableListOf<TimeData>()


    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val timeZoneApi by lazy {
        retrofit.create(TimeZoneAPI::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_world_clock, container, false)

        // Initialize RecyclerView and its adapter
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context) // Add this line
        timeZoneAdapter = TimeZoneAdapter(LayoutInflater.from(context), timeZones)
        recyclerView.adapter = timeZoneAdapter
        val itemTouchHelper = ItemTouchHelper(timeZoneAdapter.swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_worldClockFragment_to_mapsFragment)
        }

        fetchTimeZoneData("39.6606,-104.7922", System.currentTimeMillis() / 1000L)


        return view
    }

    private fun fetchTimeZoneData(location: String, timestamp: Long) {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = timeZoneApi.getTimeZone(
                location,
                timestamp,
                "AIzaSyAkFDNJKIajqqpYP6i8P3sHwTMj2VGmK5o"
            )
            if (response.isSuccessful) {
                response.body()?.let { timeData ->
                    Log.d("WorldClockFragment", "Received data: $timeData")
                    withContext(Dispatchers.Main) {
                        // Add the fetched time zone data to the adapter
                        timeZoneAdapter.addTimeZones(listOf(timeData))
                    }
                }
            } else {
                // Handle the error
                val errorMessage = response.errorBody()?.string()
                Log.e(
                    "WorldClockFragment",
                    "Error fetching time zone data: $errorMessage"
                )
            }
        }
    }
}