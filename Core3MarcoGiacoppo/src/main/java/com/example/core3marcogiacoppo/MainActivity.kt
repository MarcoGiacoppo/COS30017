package com.example.core3marcogiacoppo

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private val meetingsList = mutableListOf<Meeting>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter

    private var selectedGroup: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView)
        adapter = RecyclerViewAdapter(meetingsList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val inputStream = resources.openRawResource(R.raw.groups)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        var isFirstLine = true

        val sort = findViewById<ImageView>(R.id.sort)

        while (reader.readLine().also { line = it } != null) {
            if (isFirstLine) {
                isFirstLine = false
                continue // Skip the first line
            }

            val values = line?.split(",")
            if (values?.size == 5) {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")
                val dateTime = LocalDateTime.parse(values[4], formatter)

                val meeting = Meeting(values[0].toInt(), values[1], values[2], values[3], values[4], dateTime)
                meetingsList.add(meeting)
            }
        }

        // Close the reader when done
        reader.close()

        sort.setOnClickListener{
            if (selectedGroup == "Xsports") {
                selectedGroup = null
            } else {
                selectedGroup = "Xsports"
            }

            val filteredList = if (selectedGroup != null) {
                meetingsList.filter { it.group == selectedGroup }
            } else {
                meetingsList
            }
            adapter = RecyclerViewAdapter(filteredList)

            recyclerView.adapter = adapter
        }

        // Sort the meetingsList by date/time
        meetingsList.sortBy { it.dateTimeObject }

        // Notify the adapter of the data change
        adapter.notifyDataSetChanged()

        Log.i("Size", "Meetings list size: ${meetingsList.size}")
    }
}
