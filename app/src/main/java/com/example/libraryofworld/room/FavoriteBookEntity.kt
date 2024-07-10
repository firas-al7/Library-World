package com.example.libraryofworld.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Until

@Entity("favorite_table")
data class FavoriteBookEntity(

    @PrimaryKey(true)
    var bookId : Int = 0,

    var bookTitle : String,

    var imageUrl : String,

    var readOnlineUrl : String,


)
