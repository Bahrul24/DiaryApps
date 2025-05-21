package com.example.dairyapps

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class DiaryDaoTest {

    private lateinit var db: DiaryDatabase
    private lateinit var diaryDao: DiaryDao

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, DiaryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        diaryDao = db.diaryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertDiary_and_getAll_shouldReturnInsertedDiary() {
        val diary = Diary(title = "Pagi", content = "Bangun tidur", date = "2025-05-15")
        diaryDao.insert(diary)

        val allDiaries = diaryDao.getAll()
        assertEquals(1, allDiaries.size)
        assertEquals("Pagi", allDiaries[0].title)
        assertEquals("Bangun tidur", allDiaries[0].content)
        assertEquals("2025-05-15", allDiaries[0].date)
    }

    @Test
    fun updateDiary_shouldChangeDiaryData() {
        val diary = Diary(title = "Siang", content = "Makan siang", date = "2025-05-15")
        diaryDao.insert(diary)
        val inserted = diaryDao.getAll().first()

        val updatedDiary = inserted.copy(title = "Siang Hari", content = "Makan siang dan istirahat", date = "2025-05-15")
        diaryDao.update(updatedDiary)

        val result = diaryDao.getAll().first()
        assertEquals("Siang Hari", result.title)
        assertEquals("Makan siang dan istirahat", result.content)
    }

    @Test
    fun deleteDiary_shouldRemoveDiary() {
        val diary = Diary(title = "Malam", content = "Tidur", date = "2025-05-15")
        diaryDao.insert(diary)
        val inserted = diaryDao.getAll().first()

        diaryDao.delete(inserted)
        val allDiaries = diaryDao.getAll()
        assertTrue(allDiaries.isEmpty())
    }
}