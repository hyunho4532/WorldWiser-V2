package com.hyun.worldwiser.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyun.worldwiser.R
import com.hyun.worldwiser.databinding.DialogTourSpotsInsertBinding
import com.hyun.worldwiser.model.TourSpots
import com.hyun.worldwiser.viewmodel.GoogleMapLocationViewModel

class TourSpotsPopularAdapter(
    private val context: Context,
    private val tourSpotsPopularList: List<TourSpots>,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<TourSpotsPopularAdapter.ViewHolder>() {

    private val googleMapLocationViewModel: GoogleMapLocationViewModel = GoogleMapLocationViewModel(context, null)

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
                googleMapLocationViewModel.userSpotsPopularTitle.postValue(tourSpots.title)
                googleMapLocationViewModel.userSpotsPopularAddress.postValue(tourSpots.address)
                googleMapLocationViewModel.userSpotsPopularImage.postValue(tourSpots.imageUrl)

                showTourSpotsPopularInsertDialog()
            }
        }
    }

    private fun showTourSpotsPopularInsertDialog() {
        val dialogView = DataBindingUtil.inflate<DialogTourSpotsInsertBinding>(
            LayoutInflater.from(context),
            R.layout.dialog_tour_spots_insert,
            null,
            false
        )

        googleMapLocationViewModel.userSpotsPopularTitle.observe(viewLifecycleOwner) { title ->
            dialogView.tvTourSpotDialogTitle.text = title.toString()
        }

        googleMapLocationViewModel.userSpotsPopularAddress.observe(viewLifecycleOwner) { address ->
            dialogView.tvTourSpotDialogAddress.text = address.toString()
        }

        googleMapLocationViewModel.userSpotsPopularImage.observe(viewLifecycleOwner) { image ->
            Glide.with(context)
                .load(image)
                .into(dialogView.tvTourSpotDialogImageUrl)
        }

        val builder = AlertDialog.Builder(context)
            .setView(dialogView.root)

        val tourSpotsDialog = builder.create()
        tourSpotsDialog.show()
    }
}