package com.example.libraryofworld.api

import com.google.gson.annotations.SerializedName

data class BookSearch(
    @SerializedName("title_suggest")
    val title : String,

    //take image of book
    @SerializedName("cover_i")
    var coverId: Int?,

    @SerializedName("author_name")
    val author_name:List<String>

)

