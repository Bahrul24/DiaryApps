package com.example.dairyapps

import androidx.recyclerview.widget.DiffUtil

class DiaryDiffCallback : DiffUtil.ItemCallback<DiaryItem>() {
    override fun areItemsTheSame(oldItem: DiaryItem, newItem: DiaryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DiaryItem, newItem: DiaryItem): Boolean {
        return oldItem == newItem
    }
}
