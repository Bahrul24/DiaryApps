package com.example.dairyapps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryViewModel : ViewModel() {

    var diaryDatabase: DiaryDatabase? = null

    private val _diaries = MutableLiveData<List<Diary>>()
    val diaries: LiveData<List<Diary>> get() = _diaries

    fun getDiaries() {
        viewModelScope.launch(Dispatchers.IO) {
            val allDiaries = diaryDatabase?.diaryDao()?.getAll() ?: emptyList()
            _diaries.postValue(allDiaries)
        }
    }

    fun insertDiary(diary: Diary) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryDatabase?.diaryDao()?.insert(diary)
            getDiaries()
        }
    }

    fun deleteDiary(diary: Diary) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryDatabase?.diaryDao()?.delete(diary)
            getDiaries()
        }
    }

}
