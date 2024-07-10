package com.example.libraryofworld.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.libraryofworld.api.ApiRetrofit
import com.example.libraryofworld.api.Book
import com.example.libraryofworld.api.SearchResult
import com.example.libraryofworld.api.Works
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookViewModel : ViewModel() {

    private val _recentlyAddBook = MutableLiveData<List<Book>>()
    val recentlyAddBook: LiveData<List<Book>> = _recentlyAddBook

    private val _recentlyAddBookAll = MutableLiveData<List<Book>>()
    val recentlyAddBookAll: LiveData<List<Book>> = _recentlyAddBookAll

    private val _variousBook = MutableLiveData<List<Book>>()
    val variousBook: LiveData<List<Book>> = _variousBook

    private val _variousBookAll = MutableLiveData<List<Book>>()
    val variousBookAll: LiveData<List<Book>> = _variousBookAll

    private val _mayYouLike = MutableLiveData<List<Book>>()
    val mayYouLike: LiveData<List<Book>> = _mayYouLike

    private val _allMayYouLike = MutableLiveData<List<Book>>()
    val allMayYouLike: LiveData<List<Book>> = _allMayYouLike

    private val _searchBook = MutableLiveData<List<Book>>()
    val searchBook: LiveData<List<Book>> = _searchBook


    val api = ApiRetrofit().getBookApi()

    fun loadRecentlyAddBook() {
        api.getBookTrend()
            .enqueue(object : Callback<Works> {
                override fun onResponse(call: Call<Works>, response: Response<Works>) {
                    val bookList = response.body()?.works
                    _recentlyAddBook.postValue(bookList!!)

                }

                override fun onFailure(call: Call<Works>, t: Throwable) {
                    Log.d("TAG", "RB onFailure: $t")
                }

            })
    }

    fun loadRecentlyAddBookAllList() {
        api.getBookAllAdd()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Works> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("TAG", "onSubscribeAll: ")
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "All onError: $e")
                }

                override fun onSuccess(t: Works) {
                    val book = t.works
                    _recentlyAddBookAll.postValue(book)
                }

            })
    }

    fun loadVariousBook() {
        api.getBookRandom()
            .enqueue(object : Callback<Works> {
                override fun onResponse(call: Call<Works>, response: Response<Works>) {
                    val book = response.body()?.works
                    _variousBook.postValue(book!!)
                }

                override fun onFailure(call: Call<Works>, t: Throwable) {
                    Log.d("TAG", "VB onFailure: $t")
                }

            })
    }

    fun loadVariousBookAllList() {
        api.getBookAllRandom()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Works> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("TAG", "onSubscribeAll: ")
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "All onError: $e")
                }

                override fun onSuccess(t: Works) {
                    val book = t.works
                    _variousBookAll.postValue(book)
                }

            })
    }

    fun loadMayYouLike() {
        api.mayYouLiked()
            .enqueue(object : Callback<Works> {
                override fun onResponse(call: Call<Works>, response: Response<Works>) {
                    val book = response.body()?.works
                    _mayYouLike.postValue(book!!)
                }

                override fun onFailure(call: Call<Works>, t: Throwable) {
                    Log.d("TAG", "MayYouLike onFailure: $t")
                }

            })
    }

    fun loadMayYouLikeAllList() {
        api.mayYouLikedAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Works> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("TAG", "onSubscribeAll: ")
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "All onError: $e")

                }

                override fun onSuccess(t: Works) {
                    val book = t.works
                    _allMayYouLike.postValue(book)
                }

            })
    }

    fun loadSearchBook(searchBook: String) {
        api.searchBook(searchBook)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<SearchResult> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("TAG", "search onSubscribe: ")
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "search onError: $e")

                }

                override fun onSuccess(t: SearchResult) {
                    val book = t.docs
                    _searchBook.postValue(book)
                }

            })
    }


}
