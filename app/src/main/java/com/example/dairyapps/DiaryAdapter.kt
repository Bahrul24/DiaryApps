package com.example.dairyapps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DiaryAdapter : ListAdapter<DiaryItem, RecyclerView.ViewHolder>(DiaryDiffCallback()) {

    companion object {
        const val TYPE_ENTRY = 0
        const val TYPE_HEADER = 1
    }

    class EntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHeader: TextView = view.findViewById(R.id.tvHeader)
    }

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
                val view = inflater.inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_diary, parent, false)
                EntryViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is DiaryItem.Entry -> {
                val entry = item.diary
                (holder as EntryViewHolder).apply {
                    tvTitle.text = entry.title
                    tvContent.text = entry.content
                    tvDate.text = entry.date
                }
            }
            is DiaryItem.Header -> {
                (holder as HeaderViewHolder).tvHeader.text = item.title
            }
        }
    }
}
