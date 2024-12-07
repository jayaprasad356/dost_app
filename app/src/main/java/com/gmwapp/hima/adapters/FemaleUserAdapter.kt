package com.gmwapp.hima.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gmwapp.hima.R
import com.gmwapp.hima.callbacks.OnItemSelectionListener
import com.gmwapp.hima.databinding.AdapterFemaleUserBinding
import com.gmwapp.hima.databinding.AdapterTransactionBinding
import com.gmwapp.hima.retrofit.responses.FemaleUsersResponse
import com.gmwapp.hima.retrofit.responses.FemaleUsersResponseData
import com.gmwapp.hima.retrofit.responses.Interests
import com.gmwapp.hima.retrofit.responses.TransactionsResponseData
import com.gmwapp.hima.utils.Helper
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class FemaleUserAdapter(
    val activity: Activity,
    private val femaleUsers: List<FemaleUsersResponseData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemHolder = ItemHolder(
            AdapterFemaleUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        return itemHolder
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val femaleUser: FemaleUsersResponseData = femaleUsers[position]
        Glide.with(activity).load(femaleUser.image).apply(RequestOptions.bitmapTransform(
            RoundedCorners(28)
        )).into(holder.binding.ivProfile)

        holder.binding.tvName.text = femaleUser.name
        holder.binding.tvLanguage.text = femaleUser.language
        val interestsAsString = femaleUser.interests.split(",")
        val staggeredGridLayoutManager = FlexboxLayoutManager(activity).apply {
            flexWrap = FlexWrap.WRAP
            alignItems = AlignItems.FLEX_START
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        val itemDecoration = FlexboxItemDecoration(activity).apply {
            setDrawable(ContextCompat.getDrawable(activity, R.drawable.bg_divider))
            setOrientation(FlexboxItemDecoration.VERTICAL)
        }
        holder.binding.rvInterests.addItemDecoration(itemDecoration)
        holder.binding.rvInterests.setLayoutManager(staggeredGridLayoutManager)
        var interests = arrayListOf<Interests>()
        interestsAsString.forEach({
            interests.add(Helper.getInterestObject(activity, it))
        })
        var interestsListAdapter = InterestsFemaleListAdapter(activity, interests, false, object :
            OnItemSelectionListener<Interests> {
            override fun onItemSelected(interest: Interests) {
            }
        })
        holder.binding.rvInterests.setAdapter(interestsListAdapter)
        holder.binding.tvSummary.text = femaleUser.describe_yourself
    }

     override fun getItemCount(): Int {
        return femaleUsers.size
    }

    internal class ItemHolder(val binding: AdapterFemaleUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
