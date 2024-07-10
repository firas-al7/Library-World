package com.example.libraryofworld.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryofworld.R
import com.example.libraryofworld.adapter.AdapterRecyclerViewFavorite
import com.example.libraryofworld.mvvm.BookViewModel
import com.example.libraryofworld.room.BookDataBase
import com.example.libraryofworld.room.DownloadBookEntity
import com.example.libraryofworld.room.FavoriteBookEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class FavoritesBookFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookViewModel: BookViewModel
    private lateinit var transaction: FragmentTransaction
    private lateinit var adapter: AdapterRecyclerViewFavorite
    private lateinit var TVnoBook: TextView
    private lateinit var bookDataBase: BookDataBase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_favorites_book, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    try{
        TVnoBook = view.findViewById(R.id.textView_noFavroiteBook)

        adapter = AdapterRecyclerViewFavorite()
        recyclerView = view.findViewById(R.id.recyclerView_my_favorite)!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        transaction = requireActivity().supportFragmentManager.beginTransaction()
        bookViewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]

        bookDataBase = BookDataBase.getInstance(requireActivity())

            bookDataBase.bookDao().getBook()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<FavoriteBookEntity>> {
                    override fun onSubscribe(d: Disposable) {
                        Log.d("TAG", "onSubscribe: ")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("TAG", "BookDataBase onError: $e")
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onSuccess(book: List<FavoriteBookEntity>) {
                            adapter.bookList = book
                            adapter.notifyDataSetChanged()

                            adapter.itemOnClickDelete = {it ->
                                showMassage("Are your sure want deleted from favorite ",it)

                            }

                            adapter.itemOnClickRead = {
                                readOnlineBook(it.readOnlineUrl)
                            }
                    }

                })

        } catch (e: Exception) {
            Log.d("TAG", "favoritesPage onViewCreated: $e")
        }

    }


    fun readOnlineBook(bookReadOnlineUrl : String){
        if (bookReadOnlineUrl.isNotEmpty()) {
            val bundle = Bundle()
            bundle.putString("bookUrl", bookReadOnlineUrl)

            val onlineReadFragment = ReadOnlinePageFragment()
            onlineReadFragment.arguments = bundle

            transaction.replace(R.id.frLy, onlineReadFragment).addToBackStack(null).commit()
        }else{
            Toast.makeText(requireContext(), "sorry can't read this book", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showMassage(massage : String,it:FavoriteBookEntity){
        AlertDialog.Builder(requireContext())
            .setMessage(massage)
            .setPositiveButton("Delete"){ dialog, _ ->
                deleteBook(it)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()
            }
            .show()
    }
    fun deleteBook(it:FavoriteBookEntity){
        Toast.makeText(requireContext(), "Book Deleted", Toast.LENGTH_SHORT).show()
        adapter.removeItemAt(true)
        bookDataBase.bookDao().deleteBookFromFavorite(it)
            .subscribeOn(Schedulers.computation())
            .subscribe(object :CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    Log.d("TAG", "delete onSubscribe:  ${it.bookTitle}")
                }

                override fun onComplete() {
                    Log.d("TAG", "delete onComplete: ${it.bookTitle}")
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "delete onError: ")
                }

            })
     }

}
