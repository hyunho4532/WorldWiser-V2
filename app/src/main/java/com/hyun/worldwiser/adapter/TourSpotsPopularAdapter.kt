package com.hyun.worldwiser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hyun.worldwiser.R
import com.hyun.worldwiser.model.TourSpots

class TourSpotsPopularAdapter (
    private val context: Context,
    private val tourSpotsPopularList: List<TourSpots>
) : RecyclerView.Adapter<TourSpotsPopularAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourSpotsPopularAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tour_spots_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tourSpotsPopularList.size
    }

    override fun onBindViewHolder(holder: TourSpotsPopularAdapter.ViewHolder, position: Int) {
        holder.bind(tourSpotsPopularList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tourSpots: TourSpots) {
            itemView.findViewById<TextView>(R.id.tv_tour_spot_title).text = tourSpots.title
        }
    }
}