package com.example.dairyapps

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.dairyapps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val diaryViewModel: DiaryViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        diaryViewModel.diaryDatabase = DiaryDatabase.getInstance(this)

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

        // Setelah setting adapter & layoutManager
        binding.rvDiary.adapter = adapter
        binding.rvDiary.layoutManager = layoutManager

// Tambahkan ini:
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.currentList[position]
                if (item is DiaryItem.Entry) {
                    diaryViewModel.deleteDiary(item.diary)
                } else {
                    adapter.notifyItemChanged(position) // Cegah swipe pada header
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvDiary)


        diaryViewModel.diaries.observe(this) { diaries ->
            val grouped = diaries.groupBy { it.date }
            val items = mutableListOf<DiaryItem>()
            for ((date, entries) in grouped) {
                items.add(DiaryItem.Header(date))
                items.addAll(entries.map { DiaryItem.Entry(it) })
            }
            adapter.submitList(items)
        }

        binding.btnAdd.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val date = binding.etDate.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty() && date.isNotEmpty()) {
                val newDiary = Diary(title = title, content = content, date = date)
                diaryViewModel.insertDiary(newDiary)

                binding.etTitle.text.clear()
                binding.etContent.text.clear()
                binding.etDate.text.clear()
            }
        }

        diaryViewModel.getDiaries()
    }

}
