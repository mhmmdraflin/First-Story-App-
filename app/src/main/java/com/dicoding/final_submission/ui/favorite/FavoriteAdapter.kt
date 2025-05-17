package com.dicoding.final_submission.ui.favorite

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.final_submission.R
import com.dicoding.final_submission.response.ListEventsItem
import com.dicoding.final_submission.ui.finished.loadImage

class FavoriteAdapter(
    private val context: Context,
    private var favorites: List<ListEventsItem>
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemLogo: ImageView = view.findViewById(R.id.item_logo)
        val itemName: TextView = view.findViewById(R.id.item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val event = favorites[position]
        holder.itemLogo.loadImage(event.imageLogo)
        holder.itemName.text = event.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailFavoriteActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    fun updateData(newFavorites: List<ListEventsItem>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }
}