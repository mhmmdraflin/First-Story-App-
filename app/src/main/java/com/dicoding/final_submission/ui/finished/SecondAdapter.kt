package com.dicoding.final_submission.ui.finished

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.final_submission.R
import com.dicoding.final_submission.response.ListEventsItem

class SecondAdapter(private val context: Context, private var events: List<ListEventsItem>) :
    RecyclerView.Adapter<SecondAdapter.EventViewHolder>() {

    companion object {
        const val EVENT_ID = "EVENT_ID"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return EventViewHolder(view)
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.item_logo)
        val desc: TextView = itemView.findViewById(R.id.item_name)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val secEvent = events[position]

        holder.image.loadImage(secEvent.mediaCover)
        holder.desc.text = secEvent.name

        holder.itemView.setOnClickListener {
            val secIntent = Intent(context, DetailFinished::class.java)
            secIntent.putExtra(EVENT_ID, secEvent.id)
            context.startActivity(secIntent)
        }
    }

    override fun getItemCount(): Int = events.size
    fun updateData(newEvents: List<ListEventsItem>) {
        this.events = newEvents
        notifyDataSetChanged()
    }
}

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .into(this)
}