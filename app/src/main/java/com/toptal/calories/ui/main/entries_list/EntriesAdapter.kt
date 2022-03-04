package com.toptal.calories.ui.main.entries_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.hawk.Hawk
import com.toptal.calories.R
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.databinding.EntryItemAdapterLayoutBinding
import com.toptal.calories.utils.Constants

class EntriesAdapter : RecyclerView.Adapter<EntriesAdapter.EntryViewHolder>() {

    var _binding: EntryItemAdapterLayoutBinding? = null

    val entryList = mutableListOf<FoodEntry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        _binding = EntryItemAdapterLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntryViewHolder(_binding?.root!!)

    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.displayData(entryList[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: MutableList<FoodEntry>) {
        entryList.clear()
        entryList.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeEntry(item: FoodEntry, position: Int) {
        if (entryList.contains(item)) {
            entryList.remove(item)
        }
        notifyItemRemoved(position)
    }

    fun addEntry(item: FoodEntry) {
        if (!entryList.contains(item)) {
            entryList.add(item)
        }
        notifyItemInserted(entryList.size - 1)
    }

    override fun getItemCount(): Int {
        return entryList.size
    }


    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun displayData(item: FoodEntry, position: Int) {
            _binding?.eiaNameTv?.text = item.foodName
            _binding?.eiaDateTv?.text = item.timestamp.toString()
            _binding?.eiaCalorieTv?.text = item.calories.toString()

            handleCaloricLimit(item)
        }

        fun handleCaloricLimit(item: FoodEntry) {
            if (Hawk.get<Int>(Constants.HAWK_CALORIC_LIMIT_KEY) != null) {
                var caloricLimit = Hawk.get<Int>(Constants.HAWK_CALORIC_LIMIT_KEY)
                item.calories?.let { value ->
                    if (caloricLimit <= value.toInt()) {
                        _binding?.eiaClParent?.setBackgroundResource(R.drawable.entry_item_card_parent_background_drawable)
                    }
                }
            }
        }
    }


}