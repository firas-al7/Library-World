package com.example.libraryofworld.api

import com.google.gson.annotations.SerializedName

data class BookKey(
    @SerializedName("title")
    var title: String,

    @SerializedName("key")
    var key: String,

    //take image of book
    @SerializedName("covers")
    var covers: List<Int?>,

    //ia is title or path for book choose from user to read online
    @SerializedName("ia")
    var ia: List<String>
)
