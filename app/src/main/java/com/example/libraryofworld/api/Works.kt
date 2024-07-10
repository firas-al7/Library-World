
package com.example.libraryofworld.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Works(
    @SerializedName("works")
    @Expose val works:List<Book>

)
