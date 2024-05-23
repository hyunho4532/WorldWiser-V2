package com.hyun.worldwiser.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyun.worldwiser.R
import com.hyun.worldwiser.databinding.ItemUserPopularSpotsListBinding
import com.hyun.worldwiser.model.UserTourSpots
import com.hyun.worldwiser.ui.spots.SpotsDetailActivity
import com.hyun.worldwiser.util.IntentFilter
import com.hyun.worldwiser.viewmodel.TourSpotsSelectViewModel

class UserPopularTourSpotsAdapter (
    private val context: Context,
    viewModelStoreOwner: ViewModelStoreOwner,
    private val userPopularTourSpotsList: ArrayList<UserTourSpots>
) : RecyclerView.Adapter<UserPopularTourSpotsAdapter.ViewHolder>() {

    private val tourSpotsSelectViewModel: TourSpotsSelectViewModel = ViewModelProvider(viewModelStoreOwner)[TourSpotsSelectViewModel::class.java]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemUserPopularSpotsListBinding = DataBindingUtil.inflate (
            LayoutInflater.from(parent.context),
            R.layout.item_user_popular_spots_list,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userPopularTourSpotsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userPopularTourSpotsList[position])
    }

    inner class ViewHolder(val binding: ItemUserPopularSpotsListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userTourSpots: UserTourSpots) {

            binding.tvUserTourSpotsTitle.text = userTourSpots.title
            binding.tvUserTourSpotsAddress.text = userTourSpots.address

            Glide.with(context)
                .load(userTourSpots.imageUrl)
                .into(binding.ivUserTourSpotsImageurl)

            itemView.setOnClickListener {

                val intent = Intent(context, SpotsDetailActivity::class.java).apply {
                    putExtra("TourSpotsTitle", userTourSpots.title)
                    putExtra("TourSpotsAddress", userTourSpots.address)
                }
                context.startActivity(intent)
            }
        }
    }
}