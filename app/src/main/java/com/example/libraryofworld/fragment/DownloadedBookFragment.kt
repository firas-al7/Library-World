package com.example.libraryofworld.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryofworld.R
import com.example.libraryofworld.adapter.AdapterRecyclerViewDownloadBook
import com.example.libraryofworld.adapter.AdapterRecyclerViewFavorite
import com.example.libraryofworld.room.BookDataBase
import com.example.libraryofworld.room.DownloadBookEntity
import com.github.barteksc.pdfviewer.PDFView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


class DownloadedBookFragment : Fragment() {

    private lateinit var recyclerView_BookDownload: RecyclerView
    private lateinit var TextView_NoBook: TextView
    private lateinit var adapter: AdapterRecyclerViewDownloadBook
    private lateinit var bookDataBase: BookDataBase
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_downloaded_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            initUi(view)

            bookDataBase.bookDao().getBookToDownloadPage()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<DownloadBookEntity>> {
                    override fun onSubscribe(d: Disposable) {
                        Log.d("TAG", "onSubscribe: downloadPage")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("TAG", "download onError: $e")
                        progressBar.visibility = View.GONE
                        TextView_NoBook.visibility = View.VISIBLE
                        TextView_NoBook.text = "error try again later"
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onSuccess(t: List<DownloadBookEntity>) {
                        if (t.isEmpty()) {
                            progressBar.visibility = View.GONE
                            TextView_NoBook.visibility = View.VISIBLE

                        } else {
                            adapter.bookList = t
                            adapter.notifyDataSetChanged()
                            progressBar.visibility = View.GONE
                            TextView_NoBook.visibility = View.VISIBLE

                            adapter.itemOnClickDelete = {
                                showMassage("Are you sure want delete book",it)
                            }

                            adapter.itemOnClickRead = {

                                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                val readBookPage = ReadBookOfflineFragment()
                                val bundle = Bundle()
                                bundle.putString("KeyPDF", it.pdfFilePath)
                                readBookPage.arguments = bundle

                                transaction.replace(R.id.frLy, readBookPage).addToBackStack(null).commit()
                            }
                        }
                    }

                })

        } catch (e: Exception) {
            Log.d("TAG", "downloadPageError onViewCreated: $e")
        }
    }

    private fun initUi(view: View) {
        TextView_NoBook = view.findViewById(R.id.textView_noBookDownload)
        adapter = AdapterRecyclerViewDownloadBook()
        progressBar = view.findViewById(R.id.progressBar3_DownloadPage)
        recyclerView_BookDownload = view.findViewById(R.id.recyclerView_book_download)!!
        recyclerView_BookDownload.layoutManager = LinearLayoutManager(requireContext())
        recyclerView_BookDownload.adapter = adapter

        bookDataBase = BookDataBase.getInstance(requireActivity())
    }

    private fun showMassage(massage : String,it:DownloadBookEntity){
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

    private fun deleteBook(it: DownloadBookEntity) {
        adapter.removeItemAt(true)
        bookDataBase.bookDao().deleteBookFromDownloadedPage(it)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.d("TAG", "onSubscribe: deleteFromPageDownload")
                }

                override fun onComplete() {
                    Log.d("TAG", "onComplete: deleteFromPageDownload")
                    try {
                        val file = File(it.pdfFilePath)
                        if (file.exists()) {
                            val deleted = file.delete()
                            if (deleted) {
                                Log.d(
                                    "TAG",
                                    "onComplete: $deleted delete done"
                                )
                                Toast.makeText(
                                    requireContext(),
                                    "delete from storage done",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                Log.d(
                                    "TAG",
                                    "onComplete: delete from storage failed"
                                )
                                Toast.makeText(
                                    requireContext(),
                                    "delete from storage failed",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } else {
                            Log.d(
                                "TAG",
                                "onComplete: file not found in storage"
                            )
                            Toast.makeText(
                                requireContext(),
                                "file not found in storage",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Log.d("TAG", "delete from download page error : $e")
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "deleteFromPageDownload onError: $e")
                }

            })
    }

}
