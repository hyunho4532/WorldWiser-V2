package com.hyun.worldwiser.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyun.worldwiser.R
import com.hyun.worldwiser.model.TourSpots

class TourSpotsPopularAdapter (
    private val context: Context,
    private val tourSpotsPopularList: List<TourSpots>
) : RecyclerView.Adapter<TourSpotsPopularAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tour_spots_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tourSpotsPopularList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spotsPopularItem = tourSpotsPopularList[position]
        holder.bind(spotsPopularItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tourSpots: TourSpots) {

            Glide.with(context)
                .load(tourSpots.imageUrl)
                .override(700, 490)
                .into(itemView.findViewById(R.id.iv_tour_spot))

            itemView.findViewById<TextView>(R.id.tv_tour_spot_title).text = tourSpots.title
            itemView.findViewById<TextView>(R.id.tv_tour_spot_address).text = tourSpots.address

            itemView.setOnClickListener {
                showTourSpotsPopularInsertDialog()
            }
        }
    }

    private fun showTourSpotsPopularInsertDialog() {
        val bu
    }
}