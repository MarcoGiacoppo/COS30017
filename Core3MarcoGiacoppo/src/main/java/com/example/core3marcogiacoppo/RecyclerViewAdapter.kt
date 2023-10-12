package com.example.core3marcogiacoppo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val meetingsList: List<Meeting>) : RecyclerView.Adapter<RecyclerViewAdapter.MeetingViewHolder>() {

    inner class MeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMain: TextView = itemView.findViewById(R.id.textMain)
        val textMinor1: TextView = itemView.findViewById(R.id.textMinor1)
        val textMinor2: TextView = itemView.findViewById(R.id.textMinor2)
        val imageIcon: ImageView = itemView.findViewById(R.id.imageIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.meeting_row_layout, parent, false)
        return MeetingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val currentMeeting = meetingsList[position]

        holder.textMain.text = currentMeeting.group
        holder.textMinor1.text = currentMeeting.location
        holder.textMinor2.text = currentMeeting.datetime

        when (currentMeeting.type) {
            "Tech" -> holder.itemView.setBackgroundResource(R.color.meeting_color_tech)
            "Cultural" -> holder.itemView.setBackgroundResource(R.color.meeting_color_cultural)
            "Politics" -> holder.itemView.setBackgroundResource(R.color.meeting_color_politics)
            "Sport" -> holder.itemView.setBackgroundResource(R.color.meeting_color_sport)

            else -> holder.itemView.setBackgroundResource(android.R.color.transparent) // Default color
        }

        // Implement logic to show/hide the icon based on meeting type
        if (currentMeeting.location == "Online") {
            holder.imageIcon.visibility = View.VISIBLE
        } else {
            holder.imageIcon.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return meetingsList.size
    }


}
