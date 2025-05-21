package com.example.dairyapps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dairyapps.databinding.ItemDiaryBinding
import com.example.dairyapps.databinding.ItemHeaderBinding

class DiaryAdapter : ListAdapter<DiaryItem, RecyclerView.ViewHolder>(DiaryDiffCallback()) {

    companion object {
        const val TYPE_ENTRY = 0
        const val TYPE_HEADER = 1
    }

    class EntryViewHolder(val binding: ItemDiaryBinding) : RecyclerView.ViewHolder(binding.root)

    class HeaderViewHolder(val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DiaryItem.Entry -> TYPE_ENTRY
            is DiaryItem.Header -> TYPE_HEADER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = ItemHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemDiaryBinding.inflate(inflater, parent, false)
                EntryViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is DiaryItem.Entry -> {
                val diary = item.diary
                val binding = (holder as EntryViewHolder).binding
                binding.tvTitle.text = diary.title
                binding.tvContent.text = diary.content
                binding.tvDate.text = diary.date
            }
            is DiaryItem.Header -> {
                val binding = (holder as HeaderViewHolder).binding
                binding.tvHeader.text = item.title
            }
        }
    }
}
