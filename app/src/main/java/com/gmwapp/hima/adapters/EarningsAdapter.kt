package com.gmwapp.hima.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmwapp.hima.R
import com.gmwapp.hima.databinding.AdapterEarningsBinding
import com.gmwapp.hima.databinding.AdapterTransactionBinding
import com.gmwapp.hima.retrofit.responses.EarningsResponseData
import com.gmwapp.hima.retrofit.responses.TransactionsResponseData


class EarningsAdapter(
    val activity: Activity,
    private val earnings: List<EarningsResponseData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemHolder = ItemHolder(
            AdapterEarningsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        return itemHolder
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val earning: EarningsResponseData = earnings[position]

        when (earning.status) {
            0 -> {
                holder.binding.tvStatus.text = activity.getString(R.string.pending)
                holder.binding.tvStatus.setTextColor(activity.getColor(android.R.color.white))
            }
            1 -> {
                holder.binding.tvStatus.text = activity.getString(R.string.withdrawn)
                holder.binding.tvStatus.setTextColor(activity.getColor(android.R.color.holo_green_dark))
            }
            else -> {
                holder.binding.tvStatus.text = activity.getString(R.string.cancelled)
                holder.binding.tvStatus.setTextColor(activity.getColor(android.R.color.holo_red_dark))
            }
        }
        holder.binding.tvDate.text = earning.datetime
        holder.binding.tvAccount.text = earning.id.toString()
        holder.binding.tvAmount.text = earning.amount.toString()
        holder.binding.tvId.text = activity.getString(R.string.transaction_id, earning.id.toString())
    }

     override fun getItemCount(): Int {
        return earnings.size
    }

    internal class ItemHolder(val binding: AdapterEarningsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}