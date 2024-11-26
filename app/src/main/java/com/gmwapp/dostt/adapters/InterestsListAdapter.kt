package com.gmwapp.dostt.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gmwapp.dostt.R
import com.gmwapp.dostt.databinding.AdapterInterestBinding
import com.gmwapp.dostt.retrofit.responses.Interests


class InterestsListAdapter(
    val activity: Activity,
    private val interests: ArrayList<Interests>,
    var highlightedPosition: Int,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemHolder = ItemHolder(
            AdapterInterestBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        return itemHolder
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val interests: Interests = interests[position]

        holder.binding.tvInterest.text = interests.name
        holder.binding.ivInterest.setImageResource(interests.image)
        Log.e("siva", ""+position+" "+interests.name)
//        Glide.with(activity)
//            .load(activity.getDrawable(interests.image))
//            .into(holder.binding.ivInterest)


    }

    override fun getItemCount(): Int {
        return interests.size
    }

    internal class ItemHolder(val binding: AdapterInterestBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
