package com.example.libraryofworld.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity("download_table")
data class DownloadBookEntity(

    @PrimaryKey(true)
    var bookId : Int = 0,

    var bookTitle : String,

    var imageUrl : String,

    val pdfFilePath: String,

)
