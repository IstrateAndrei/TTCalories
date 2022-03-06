package com.toptal.calories.ui.main.entries_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.hawk.Hawk
import com.toptal.calories.R
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.databinding.EntryItemAdapterLayoutBinding
import com.toptal.calories.utils.*
import java.util.*

class EntriesAdapter : RecyclerView.Adapter<EntriesAdapter.EntryViewHolder>() {

    var _binding: EntryItemAdapterLayoutBinding? = null
    val binding get() = _binding!!
    val entryList = mutableListOf<FoodEntry>()

    private lateinit var listener: OnEntryClickListener

    interface OnEntryClickListener {
        fun onEntryClicked(item: FoodEntry, position: Int)
        fun onEntryDeleted(item: FoodEntry, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        _binding = EntryItemAdapterLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntryViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.displayData(entryList[position], position)
    }

    fun setupListener(listener: OnEntryClickListener) {
        this.listener = listener
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

    @SuppressLint("NotifyDataSetChanged")
    fun applyFromFilter(fromDate: Date?) {
        fromDate?.let {
            val fromFiltered = entryList.filter { item -> item.entry_date!!.after(it) }
            entryList.clear()
            entryList.addAll(fromFiltered)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return entryList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun displayData(item: FoodEntry, position: Int) {
            binding.eiaNameTv.text = item.name
            binding.eiaDateTv.text = item.entry_date?.let { getStringFromDateTime(it) }
            binding.eiaCalorieTv.text = item.calories.toString()

            handleCaloricLimit(item)
            if (hasAdminRights() && ::listener.isInitialized) {
                itemView.setOnClickListener {
                    listener.onEntryClicked(item, position)
                }

                binding.eiaDeleteIv.setOnClickListener {
                    listener.onEntryDeleted(item, position)
                }

                itemView.setOnLongClickListener {
                    binding.eiaDeleteIv.visibility =
                        getViewVisibility(!binding.eiaDeleteIv.isVisible)
                    return@setOnLongClickListener true
                }
            }
        }

        fun handleCaloricLimit(item: FoodEntry) {
            if (Hawk.get<Int>(Constants.HAWK_CALORIC_LIMIT_KEY) != null) {
                val caloricLimit = Hawk.get<Int>(Constants.HAWK_CALORIC_LIMIT_KEY)
                item.calories.let { value ->
                    if (caloricLimit <= value) {
                        _binding?.eiaClParent?.setBackgroundResource(R.drawable.entry_item_card_parent_background_drawable)
                    }
                }
            }
        }
    }

}