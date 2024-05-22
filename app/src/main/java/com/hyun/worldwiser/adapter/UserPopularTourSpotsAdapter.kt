package com.hyun.worldwiser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.hyun.worldwiser.R
import com.hyun.worldwiser.databinding.ItemUserPopularSpotsListBinding
import com.hyun.worldwiser.model.UserTourSpots
import com.hyun.worldwiser.ui.spots.SpotsDetailActivity
import com.hyun.worldwiser.util.IntentFilter
import com.hyun.worldwiser.viewmodel.TourSpotsSelectViewModel

class UserPopularTourSpotsAdapter (
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val userPopularTourSpotsList: ArrayList<UserTourSpots>,
    private val tourSpotsSelectViewModel: TourSpotsSelectViewModel
) : RecyclerView.Adapter<UserPopularTourSpotsAdapter.ViewHolder>() {

    private val spotsDetailActivity: SpotsDetailActivity = SpotsDetailActivity()
    private val intentFilter: IntentFilter = IntentFilter()

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
                intentFilter.getIntent(context, spotsDetailActivity)
            }
        }
    }
}