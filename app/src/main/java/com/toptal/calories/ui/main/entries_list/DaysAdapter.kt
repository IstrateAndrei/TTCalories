package com.toptal.calories.ui.main.entries_list

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.hawk.Hawk
import com.toptal.calories.data.model.Day
import com.toptal.calories.data.model.FoodEntry
import com.toptal.calories.databinding.DayItemLayoutBinding
import com.toptal.calories.utils.Constants

class DaysAdapter : RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {

    val list = mutableListOf<Day>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val binding =
            DayItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        holder.displayData(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: MutableList<Day>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class DaysViewHolder(private var binding: DayItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun displayData(item: Day, position: Int) {
            binding.diDateLabelTv.text = item.dateString
            val totalCals = getTotalCal(item.entriesList)
            binding.diTotalCalTv.text = totalCals.toString()
            handleCaloricLimit(totalCals)
            initAdapter(item)
        }

        fun initAdapter(item: Day) {
            if (binding.diCalRv.layoutManager == null) binding.diCalRv.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)

            if (binding.diCalRv.adapter == null) binding.diCalRv.adapter = EntriesAdapter()

            (binding.diCalRv.adapter as EntriesAdapter).updateList(item.entriesList)
        }

        fun getTotalCal(list: MutableList<FoodEntry>): Int {
            var sum = 0
            list.forEach { item ->
                sum += item.calories
            }
            return sum
        }

        fun handleCaloricLimit(totalCal: Int) {
            if (Hawk.get<Int>(Constants.HAWK_CALORIC_LIMIT_KEY) != null) {
                val caloricLimit = Hawk.get<Int>(Constants.HAWK_CALORIC_LIMIT_KEY)

                var color = ""
                if (totalCal <= caloricLimit * 0.7) {
                    //show with green
                    color = "#90EE90"
                } else if (totalCal > caloricLimit * 0.7 && totalCal < caloricLimit) {
                    //show with yellow
                    color = "#F0E68C"
                } else if (totalCal >= caloricLimit) {
                    //show with red
                    color = "#FA8072"
                }
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.BOTTOM_TOP,
                    intArrayOf(Color.WHITE, Color.parseColor(color))
                )
                gradient.cornerRadius = 10F

                ViewCompat.setBackground(binding.diParentCard, gradient)
            }
        }
    }

}