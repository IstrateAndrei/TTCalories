package com.toptal.calories.ui.admin.reports

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toptal.calories.data.model.AverageCals
import com.toptal.calories.databinding.AverageCalItemLayoutBinding

class AverageCalAdapter : RecyclerView.Adapter<AverageCalAdapter.AverageCalViewHolder>() {

    var _binding: AverageCalItemLayoutBinding? = null
    val binding get() = _binding!!
    val avgCalList = mutableListOf<AverageCals>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AverageCalViewHolder {
        _binding = AverageCalItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AverageCalViewHolder(binding.root)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: AverageCalViewHolder, position: Int) {
        holder.displayData(avgCalList[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: MutableList<AverageCals>) {
        avgCalList.clear()
        avgCalList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return avgCalList.size
    }

    inner class AverageCalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun displayData(item: AverageCals, position: Int) {
            binding.aciUserTv.text = item.userName
            binding.aciCalValueTv.text = item.avg.toString()
        }

    }


}