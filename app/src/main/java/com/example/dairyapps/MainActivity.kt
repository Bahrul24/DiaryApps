package com.example.dairyapps

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val diaryViewModel: DiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diaryViewModel.diaryDatabase = DiaryDatabase.getInstance(this)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val etDate = findViewById<EditText>(R.id.etDate)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val rvDiary = findViewById<RecyclerView>(R.id.rvDiary)

        val adapter = DiaryAdapter()
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    DiaryAdapter.TYPE_HEADER -> 2
                    else -> 1
                }
            }
        }

        rvDiary.adapter = adapter
        rvDiary.layoutManager = layoutManager

        diaryViewModel.diaries.observe(this) { diaries ->
            val grouped = diaries.groupBy { it.date }
            val items = mutableListOf<DiaryItem>()
            for ((date, entries) in grouped) {
                items.add(DiaryItem.Header(date))
                items.addAll(entries.map { DiaryItem.Entry(it) })
            }
            adapter.submitList(items)
        }

        btnAdd.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()
            val date = etDate.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty() && date.isNotEmpty()) {
                val newDiary = Diary(title = title, content = content, date = date)
                diaryViewModel.insertDiary(newDiary)

                etTitle.text.clear()
                etContent.text.clear()
                etDate.text.clear()
            }
        }

        diaryViewModel.getDiaries()
    }
}
