package com.gmwapp.dostt.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmwapp.dostt.R
import com.gmwapp.dostt.callbacks.OnItemSelectionListener
import com.gmwapp.dostt.databinding.AdapterLanguageBinding
import com.gmwapp.dostt.retrofit.responses.Language


class LanguageAdapter(
    val activity: Activity,
    private val languages: ArrayList<Language>,
    val onItemSelectionListener: OnItemSelectionListener<Language>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemHolder = ItemHolder(
            AdapterLanguageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        return itemHolder
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val language: Language = languages[position]

        holder.binding.tvLanguage.text = language.name
        holder.binding.ivLanguage.setImageResource(language.image)
        if (language.isSelected == true) {
            holder.binding.main.setBackgroundResource(R.drawable.d_button_bg_language_selected)
        } else {
            holder.binding.main.setBackgroundResource(R.drawable.d_button_bg_language)
        }
        holder.binding.main.setOnClickListener {
            onItemSelectionListener.onItemSelected(language)
            languages.onEach { it.isSelected = false }
            language.isSelected = true
            languages[position] = language
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return languages.size
    }

    internal class ItemHolder(val binding: AdapterLanguageBinding) :
        RecyclerView.ViewHolder(binding.root)
}
