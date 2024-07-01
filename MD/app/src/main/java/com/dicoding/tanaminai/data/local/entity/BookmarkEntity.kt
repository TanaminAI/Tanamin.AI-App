package com.dicoding.tanaminai.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "image")
    var image: Int,

    @ColumnInfo(name = "predictAt")
    var predictAt: String,

    @ColumnInfo(name = "result")
    var result: String,

    @ColumnInfo(name = "n")
    var n: String,

    @ColumnInfo(name = "p")
    var p: String,

    @ColumnInfo(name = "k")
    var k: String,

    @ColumnInfo(name = "hum")
    var hum: String,

    @ColumnInfo(name = "ph")
    var ph: String,

    @ColumnInfo(name = "temp")
    var temp: String,

    )
