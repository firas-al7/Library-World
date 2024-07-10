package com.example.libraryofworld.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface BookDao {

    @Insert
    fun insertBookToFavorite(book : FavoriteBookEntity) : Completable

    @Query("select * from favorite_table")
    fun getBook():Single<List<FavoriteBookEntity>>

    @Delete
    fun deleteBookFromFavorite(bookId : FavoriteBookEntity):Completable

    @Insert
     fun insertPdfFile(pdfFile: DownloadBookEntity):Completable

    @Query("select * from download_table")
    fun getBookToDownloadPage():Single<List<DownloadBookEntity>>

    @Delete
    fun deleteBookFromDownloadedPage(bookId : DownloadBookEntity):Completable

}
