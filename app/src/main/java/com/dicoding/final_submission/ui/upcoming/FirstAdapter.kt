package com.dicoding.final_submission.ui.upcoming

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.final_submission.R
import com.dicoding.final_submission.response.ListEventsItem

class FirstAdapter(
    private val context: Context,
    private var dataList: List<ListEventsItem>,
    private val itemClickListener: (Int) -> Unit // Listener for event ID
) : RecyclerView.Adapter<FirstAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventName: TextView = itemView.findViewById(R.id.item_name)
        private val eventLogo: ImageView = itemView.findViewById(R.id.item_logo)

        fun bind(event: ListEventsItem) {
            eventName.text = event.name
            Glide.with(context).load(event.imageLogo).into(eventLogo)

            itemView.setOnClickListener {
                itemClickListener(event.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size

    fun updateData(newData: List<ListEventsItem>) {
        dataList = newData
        notifyDataSetChanged()
    }
}