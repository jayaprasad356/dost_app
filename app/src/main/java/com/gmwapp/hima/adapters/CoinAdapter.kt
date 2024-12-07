package com.gmwapp.hima.adapters

import android.app.Activity
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmwapp.hima.R
import com.gmwapp.hima.callbacks.OnItemSelectionListener
import com.gmwapp.hima.databinding.AdapterCoinBinding
import com.gmwapp.hima.retrofit.responses.CoinsResponseData


class CoinAdapter(
    val activity: Activity,
    private val coins: ArrayList<CoinsResponseData>,
    val onItemSelectionListener: OnItemSelectionListener<CoinsResponseData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemHolder = ItemHolder(
            AdapterCoinBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        return itemHolder
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val coin: CoinsResponseData = coins[position]

        if(coin.isSelected == true){
            holder.binding.main.setBackgroundResource(R.drawable.d_button_bg_coin_selected)
        } else{
            holder.binding.main.setBackgroundResource(R.drawable.d_button_bg_coin)
        }
        holder.binding.main.setOnClickListener {
            onItemSelectionListener.onItemSelected(coin)
            coins.onEach { it.isSelected = false }
            coin.isSelected = true
            coins[position] = coin
            notifyDataSetChanged()
        }
        holder.binding.tvCoins.text = coin.coins.toString()
        if (coin.discount_price == null) {
            holder.binding.tvDiscountPrice.visibility = View.GONE
        } else {
            holder.binding.tvDiscountPrice.paintFlags = holder.binding.tvDiscountPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.tvDiscountPrice.visibility = View.VISIBLE
            holder.binding.tvDiscountPrice.text = activity.getString(R.string.rupee_text,coin.discount_price)
        }
        holder.binding.tvPrice.text = activity.getString(R.string.rupee_text,coin.price)
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    internal class ItemHolder(val binding: AdapterCoinBinding) :
        RecyclerView.ViewHolder(binding.root)
}
