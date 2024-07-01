package com.dicoding.tanaminai.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.tanaminai.data.local.entity.BookmarkEntity

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookmark: BookmarkEntity)

    @Query("DELETE FROM BookmarkEntity WHERE predictAt = :predictAt")
    suspend fun delete(predictAt: String?)

    @Query("SELECT * from BookmarkEntity ORDER BY id DESC")
    fun getAllBookmark(): LiveData<List<BookmarkEntity>>

    @Query("SELECT COUNT(*) FROM BookmarkEntity WHERE predictAt = :predictAt")
    fun checkBookmark(predictAt: String?): LiveData<Int>

}