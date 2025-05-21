package com.example.dairyapps

sealed class DiaryItem {
    abstract val id: Int

    data class Entry(val diary: Diary) : DiaryItem() {
        override val id: Int get() = diary.id
    }

    data class Header(val title: String) : DiaryItem() {
        override val id: Int = title.hashCode()
    }
}
