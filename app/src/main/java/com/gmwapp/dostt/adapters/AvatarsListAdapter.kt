package com.gmwapp.dostt.adapters

import android.app.Activity
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gmwapp.dostt.R
import com.gmwapp.dostt.activities.EditProfileActivity
import com.gmwapp.dostt.databinding.AdapterAvatarBinding
import com.gmwapp.dostt.retrofit.responses.AvatarsListData
import kotlin.math.abs


class AvatarsListAdapter(
    val activity: Activity,
    private val avatarsListData: ArrayList<AvatarsListData?>,
    var highlightedPosition: Int,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemHolder = ItemHolder(
            AdapterAvatarBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        return itemHolder
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val avatarsListData: AvatarsListData? = avatarsListData[position]
        if (avatarsListData == null) {
            if (activity is EditProfileActivity) {
                holder.binding.root.setBackgroundColor(activity.getColor(R.color.black_background))
            } else {
                holder.binding.root.setBackgroundColor(activity.getColor(R.color.blue_background))
            }
        } else {
            Glide.with(activity).load(avatarsListData.image)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .into(holder.binding.ivAvatar)

        }
    }

    override fun getItemCount(): Int {
        return avatarsListData.size
    }

    internal class ItemHolder(val binding: AdapterAvatarBinding) :
        RecyclerView.ViewHolder(binding.root)
}