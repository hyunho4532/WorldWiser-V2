package com.hyun.worldwiser.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.hyun.worldwiser.R
import com.hyun.worldwiser.databinding.DialogTourSpotsSelectBinding
import com.hyun.worldwiser.databinding.ItemUserPopularSpotsListBinding
import com.hyun.worldwiser.model.UserTourSpots
import com.hyun.worldwiser.viewmodel.TourSpotsSelectViewModel
import java.security.PrivateKey

class UserPopularTourSpotsAdapter (
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val userPopularTourSpotsList: ArrayList<UserTourSpots>,
    private val tourSpotsSelectViewModel: TourSpotsSelectViewModel
) : RecyclerView.Adapter<UserPopularTourSpotsAdapter.ViewHolder>() {

    private lateinit var tourSpotsGoogleMap: GoogleMap

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

                tourSpotsSelectViewModel.setTourSpotsTitle(userTourSpots.title)

                val tourSpotsDialogBinding: DialogTourSpotsSelectBinding = DataBindingUtil.inflate (
                    LayoutInflater.from(context),
                    R.layout.dialog_tour_spots_select,
                    null,
                    false
                )

                tourSpotsDialogBinding.tourSpotsSelectViewModel = tourSpotsSelectViewModel
                tourSpotsDialogBinding.lifecycleOwner = lifecycleOwner

                val tourSpotsDialog = AlertDialog.Builder(context)
                    .setView(tourSpotsDialogBinding.root)
                    .create()

                tourSpotsDialog.show()

                val mapFragment = SupportMapFragment.newInstance()
                val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.map, mapFragment)
                transaction.commit()

                mapFragment.getMapAsync(OnMapReadyCallback { googleMap ->
                    tourSpotsGoogleMap = googleMap

                    val sydney = com.google.android.gms.maps.model.LatLng

                    googleMap.addMarker(MarkerOptions()
                        .position(sydney)
                        .title("Marker"))

                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                })
            }
        }
    }
}