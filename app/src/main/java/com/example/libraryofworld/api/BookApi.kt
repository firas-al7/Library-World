
package com.example.libraryofworld.api

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface BookApi {

    //for ui in Recently added books
    @GET("trending/now.json?limit=13")
    fun getBookTrend(): Call<Works>

    //if click on Recently added books take all book
    @GET("trending/now.json")
    fun getBookAllAdd(): Single<Works>

    @GET("trending/weekly.json?limit=13")
    fun getBookRandom(): Call<Works>

    @GET("trending/weekly.json")
    fun getBookAllRandom(): Single<Works>

    @GET("trending/subjects/fantasy.json?limit=13")
    fun mayYouLiked(): Call<Works>

    @GET("trending/subjects/fantasy.json")
    fun mayYouLikedAll(): Single<Works>

    @GET("works/{key}.json")
    fun getKeyBook(@Path("key")key:String):Call<Book>

    @GET("works/{key}/ratings.json")
    fun getKeyBookRata(@Path("key")key:String):Call<Rata>

    @GET("search.json?limit=10")
    fun searchBook(@Query("q") query : String):Single<SearchResult>

}
