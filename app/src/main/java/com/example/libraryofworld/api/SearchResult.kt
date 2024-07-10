package com.example.libraryofworld.api

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("docs") val docs : List<Book>
)
