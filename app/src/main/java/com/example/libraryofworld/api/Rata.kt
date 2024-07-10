package com.example.libraryofworld.api

import com.google.gson.annotations.SerializedName

data class Rata(
    @SerializedName("counts")
    var counts:Counts
)