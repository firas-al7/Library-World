package com.example.libraryofworld.api

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@SuppressLint("ParcelCreator")
data class Book(

    @SerializedName("title")
    var title: String,

    @SerializedName("author_name")
    var author_name: List<String>,

    @SerializedName("key")
    var key: String,

    //take image of book
    @SerializedName("cover_i")
    var coverId: Int?,

    @SerializedName("description")
    var description: String,

    @SerializedName("ratings")
    var ratings: Rata,

    //ia is title or path for book choose from user to read online
    @SerializedName("ia")
    var ia: List<String>

) : Serializable
