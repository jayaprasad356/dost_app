package com.gmwapp.hima.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmwapp.hima.R
import com.gmwapp.hima.callbacks.OnItemSelectionListener
import com.gmwapp.hima.databinding.AdapterDeleteReasonsBinding
import com.gmwapp.hima.retrofit.responses.Reason


class DeleteReasonAdapter(
    val activity: Activity,
    private val reasons: ArrayList<Reason>,
    private var isOther: Boolean,
    val onItemSelectionListener: OnItemSelectionListener<Reason>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemHolder = ItemHolder(
            AdapterDeleteReasonsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        return itemHolder
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val reason: Reason = reasons[position]

        holder.binding.main.setOnClickListener(View.OnClickListener {
            onItemSelectionListener.onItemSelected(reason)
            if (reason.name == "Other") {
                isOther = true
            }
            reason.isSelected = reason.isSelected == null || reason.isSelected == false
            reasons[position] = reason
            notifyDataSetChanged()
        })
        if (isOther) {
            if (reason.name == "Other") {
                holder.binding.main.isEnabled = true
                holder.binding.main.setBackgroundResource(R.drawable.d_button_bg_interest_selected)
                holder.binding.tvReason.setTextColor(activity.getColor(R.color.black))
            } else {
                holder.binding.main.isEnabled = false
                holder.binding.main.setBackgroundResource(R.drawable.d_button_bg_interest_disabled)
                holder.binding.tvReason.setTextColor(activity.getColor(R.color.interest_disabled_text_color))
            }
        } else if (reason.isSelected == true) {
            holder.binding.main.isEnabled = true
            holder.binding.main.setBackgroundResource(R.drawable.d_button_bg_interest_selected)
            holder.binding.tvReason.setTextColor(activity.getColor(R.color.black))
        } else {
            holder.binding.main.isEnabled = true
            holder.binding.main.setBackgroundResource(R.drawable.d_button_bg_interest)
            holder.binding.tvReason.setTextColor(activity.getColor(R.color.interest_text_color))
        }
        holder.binding.tvReason.text = reason.name
    }

    override fun getItemCount(): Int {
        return reasons.size
    }

    internal class ItemHolder(val binding: AdapterDeleteReasonsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
