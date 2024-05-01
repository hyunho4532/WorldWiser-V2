package com.hyun.worldwiser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyun.worldwiser.R
import com.hyun.worldwiser.model.UserTourSpots
import org.w3c.dom.Text

class UserPopularTourSpotsAdapter(
    private val context: Context,
    private val userPopularTourSpotsList: ArrayList<UserTourSpots>
) : RecyclerView.Adapter<UserPopularTourSpotsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_popular_spots_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userPopularTourSpotsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userPopularTourSpotsList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userTourSpots: UserTourSpots) {
            itemView.findViewById<TextView>(R.id.tv_user_tour_spots_title).text = userTourSpots.title
            itemView.findViewById<TextView>(R.id.tv_user_tour_spots_address).text = userTourSpots.address

            Glide.with(context)
                .load(userTourSpots.imageUrl)
                .into(itemView.findViewById(R.id.iv_user_tour_spots_imageurl))
        }
    }
}