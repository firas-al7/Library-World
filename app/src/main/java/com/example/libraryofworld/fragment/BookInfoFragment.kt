
package com.example.libraryofworld.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryofworld.R
import com.example.libraryofworld.adapter.AdapterBook
import com.example.libraryofworld.api.ApiRetrofit
import com.example.libraryofworld.api.Book
import com.example.libraryofworld.api.BookApi
import com.example.libraryofworld.api.Rata
import com.example.libraryofworld.mvvm.BookViewModel
import com.example.libraryofworld.room.BookDataBase
import com.example.libraryofworld.room.DownloadBookEntity
import com.example.libraryofworld.room.FavoriteBookEntity
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat

@Suppress("DEPRECATION")
class BookInfoFragment : Fragment() {

    private var isBookInFavorite = false
    private lateinit var bookDataBase: BookDataBase
    private val STORAGE_DIRECTORY= "/Download/LibraryBook/"

    private lateinit var imageBook: ImageView
    private lateinit var nameBook: TextView
    private lateinit var nameAuthor: TextView
    private lateinit var description: TextView
    private lateinit var ratingInNumTV: TextView
    private lateinit var mayYouLikeBlock: TextView
    private lateinit var numberDownload: TextView
    private lateinit var readOnlineBTN: Button
    private lateinit var downloadBookBTN: Button
    private lateinit var saveBookBTN: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookViewModel: BookViewModel
    private lateinit var ratingBar: RatingBar
    private lateinit var error: String
    private lateinit var addFavorite: String
    private lateinit var removeFavorite: String
    private lateinit var api: BookApi
    private lateinit var transaction: FragmentTransaction
    private lateinit var bookInfoKey: Book
    private lateinit var bundle: Bundle
    private lateinit var selectKey: String
    private var bookTitle: String = ""
    private lateinit var imageUrl: Unit
    private var bookReadOnlineUrl: String = ""
    private lateinit var imageUrlForBook: String
    private lateinit var progressBar_download: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_book_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookInfoKey = arguments?.getSerializable(KEY) as Book

        try {

            initialization(view)

            regexClean(bookInfoKey.key, bookInfoKey)
            mayYouLike()

            mayYouLikeBlock.setOnClickListener {
                val forAllFragmentPage = ForAllFragment()
                bundle.putString(KEY, "mayYouLike")
                forAllFragmentPage.arguments = bundle

                transaction.replace(R.id.frLy, forAllFragmentPage).addToBackStack(null).commit()
            }

            if (bookTitle.isNotEmpty()) {
                readOnlineBTN.setOnClickListener {

                    bookReadOnlineUrl =
                        "https://archive.org/details/$bookTitle/mode/2up?view=theater"

                    bundle.putString("bookUrl", bookReadOnlineUrl)

                    val onlineReadFragment = ReadOnlinePageFragment()
                    onlineReadFragment.arguments = bundle

                    transaction.replace(R.id.frLy, onlineReadFragment).addToBackStack(null).commit()
                }
            } else {
                readOnlineBTN.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        "Sorry can't read this book!! ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            //save book in favorite
            try {
                saveBookBTN.setOnClickListener {
                    addOrRemoveFavorite()
                }
            } catch (e: Exception) {
                Log.d("TAG", "bookInfo saveBookBTN onViewCreated: $e")
            }

            //download Book
            try {
                downloadBookBTN.setOnClickListener {
                    Toast.makeText(requireContext(), "Starting Downloaded", Toast.LENGTH_SHORT)
                        .show()
                    val iaList = bookInfoKey.ia
                    val list = iaList[0]
                    Log.d("TAG", "list onViewCreated: $list")

                    if (list.isNotEmpty()) {
                        Log.d("TAG", " booktitle onViewCreated: $list")
                        startDownloading(
                            "https://archive.org/download/$list/$list.pdf",
                            "$list.pdf"
                        )

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Can't download this book",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                Log.d("TAG", "downloadBook onViewCreated: $e")
            }

        } catch (e: Exception) {
            Log.d("TAG", "BookInfo onViewCreated: $e")
        }
    }

    private fun initialization(view: View) {

        api = ApiRetrofit().getBookApi()
        transaction = requireActivity().supportFragmentManager.beginTransaction()
        bookInfoKey = arguments?.getSerializable(KEY) as Book

        val iaList = bookInfoKey.ia
        val formatNameIA = iaList.joinToString("")
        bookTitle = formatNameIA

        bundle = Bundle()

        //download_thing
        numberDownload = view.findViewById(R.id.number_download)
        progressBar_download = view.findViewById(R.id.progressBar_download)
        progressBar_download.visibility = View.GONE

        imageBook = view.findViewById(R.id.imageView_Book_in_info)!!
        nameBook = view.findViewById(R.id.name_book_in_infoTV)!!
        nameAuthor = view.findViewById(R.id.name_author_in_infoTV)!!
        description = view.findViewById(R.id.description_textView)!!
        mayYouLikeBlock = view.findViewById(R.id.may_you_likeTV)!!

        readOnlineBTN = view.findViewById(R.id.read_online_button)!!
        downloadBookBTN = view.findViewById(R.id.download_button)!!
        saveBookBTN = view.findViewById(R.id.save_favorites_button)!!

        progressBar = view.findViewById(R.id.progressBar_may_you_like)!!
        recyclerView = view.findViewById(R.id.recyclerView_may_you_like)!!
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        bookViewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]

        ratingBar = view.findViewById(R.id.ratingBar)!!
        ratingBar.scaleX = 0.5F
        ratingBar.scaleY = 0.5F
        ratingInNumTV = view.findViewById(R.id.rateingIntTV)!!

        error = context?.getString(R.string.error)!!
        addFavorite = context?.getString(R.string.doneFavorite)!!
        removeFavorite = context?.getString(R.string.favorite)!!

        bookDataBase = BookDataBase.getInstance(requireActivity())

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun mayYouLike() {
        bookViewModel.loadMayYouLike()
        bookViewModel.mayYouLike.observe(viewLifecycleOwner) { book ->

            val adapter = AdapterBook(book)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE

            adapter.itemOnClick = {
                val bookInfoFragment = BookInfoFragment()
                val bundle = Bundle()
                bundle.putSerializable(KEY, it)
                bookInfoFragment.arguments = bundle

                transaction.replace(R.id.frLy, bookInfoFragment).addToBackStack(null).commit()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun regexClean(key: String, bookInfoKey: Book) {

        // used regex because key will take is just after works/ to take description and rating book
        val regex = Regex("/works/(.+)")
        val matchResult = regex.find(key)
        val finalKey = matchResult?.groupValues?.get(1)
        bookInfo(bookInfoKey, finalKey!!)
        selectKey = finalKey


    }

    private fun bookInfo(bookInfoKey: Book, keyBook: String) {

        //used format because if no do it will output [name author] cause name from list
        val formatName = bookInfoKey.author_name.joinToString("")

        nameBook.text = bookInfoKey.title
        nameAuthor.text = formatName
        imageUrl = bookInfoKey.coverId?.let { coverId ->
            imageUrlForBook = "https://covers.openlibrary.org/b/id/$coverId-L.jpg"
            Picasso.get().load(imageUrlForBook).into(imageBook)
        }!!


        descriptionBook(keyBook)
        ratingBook(keyBook)

    }

    private fun ratingBook(keyBook: String) {

        api.getKeyBookRata(keyBook).enqueue(object : Callback<Rata> {
            override fun onResponse(call: Call<Rata>, response: Response<Rata>) {
                val rateBook = response.body()?.counts
                val star1 = rateBook?._1
                val star2 = rateBook?._2
                val star3 = rateBook?._3
                val star4 = rateBook?._4
                val star5 = rateBook?._5

                val map = mapOf(1 to star1, 2 to star2, 3 to star3, 4 to star4, 5 to star5)
                var totalVotes = 0
                var totalWeightedSum = 0

                for ((rating, count) in map) {
                    totalVotes += count!!
                    totalWeightedSum += rating * count

                }
                val averageRating = if (totalVotes > 0) {
                    totalWeightedSum.toFloat() / totalVotes
                } else {
                    Log.d("TAG", "nothing onResponse: ")
                }
                ratingBar.rating = averageRating.toFloat()

                val decimalFormat = DecimalFormat("#.#")
                val formattedValue = decimalFormat.format(averageRating)
                ratingInNumTV.text = formattedValue.toString()

            }

            override fun onFailure(call: Call<Rata>, t: Throwable) {
                Log.d("TAG", "rate onFailure: $t")
            }

        })
    }

    private fun descriptionBook(keyBook: String) {

        api.getKeyBook(keyBook).enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                if (response.isSuccessful) {
                    val des = response.body()?.description
                    description.text = des ?: "No description available "

                } else {
                    Log.d("TAG", "onResponse: not successful des ${response.body()}")
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                Log.d("TAG", "des onFailure: $t")
            }

        })
    }

    private fun addOrRemoveFavorite() {
        try {
            Log.d("TAG", "addOrRemoveFavorite: $imageUrlForBook")
            isBookInFavorite = !isBookInFavorite

            saveBookBTN.text = if (isBookInFavorite) {
                bookDataBase.bookDao().insertBookToFavorite(
                    FavoriteBookEntity(
                        0,
                        nameBook.text.toString(),
                        imageUrlForBook,
                        bookReadOnlineUrl,
                    )

                )
                    .subscribeOn(Schedulers.computation())
                    .subscribe(object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {}

                        override fun onComplete() {
                            Log.d("TAG", "onComplete: bookInfo")
                        }

                        override fun onError(e: Throwable) {
                            Log.e("TAG", "Error adding Book", e)
                        }
                    })

                Toast.makeText(requireContext(), "Added Book", Toast.LENGTH_SHORT).show()

                addFavorite
            } else {

                Toast.makeText(requireContext(), "Removed Book", Toast.LENGTH_SHORT).show()
                removeFavorite

            }
        } catch (e: Exception) {
            Log.d("TAG", "addOrRemoveFavorite: $e")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun startDownloading(mUrl: String, fileName: String) {

        try {
            val storageDirectory =
                Environment.getExternalStorageDirectory()
                    .toString() + STORAGE_DIRECTORY + fileName

            val file =
                File(Environment.getExternalStorageDirectory().toString() + STORAGE_DIRECTORY)
            if (!file.exists()) {
                file.mkdirs()
            }

            GlobalScope.launch(Dispatchers.IO) {
                val url = URL(mUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Accept-Encoding", "identity")
                connection.connect()

                if (connection.responseCode == 200) {

                    val fileSize = connection.contentLength
                    val inputStream = connection.inputStream
                    val outputStream = FileOutputStream(storageDirectory)

                    var bytesCopied: Long = 0
                    val buffer = ByteArray(1024)
                    var byte = inputStream.read(buffer)
                    while (byte >= 0) {
                        bytesCopied += byte
                        val downloadProgress =
                            (bytesCopied.toFloat() / fileSize.toFloat() * 100).toInt()
                        withContext(Dispatchers.Main) {
                            progressBar_download.visibility = View.VISIBLE
                            numberDownload.visibility = View.VISIBLE

                            progressBar_download.progress = downloadProgress
                            numberDownload.text = "$downloadProgress"
                            if (downloadProgress == 100) {

                                val bookDownload = DownloadBookEntity(
                                    0,
                                    nameBook.text.toString(),
                                    imageUrlForBook,
                                    storageDirectory,
                                )


                                bookDataBase.bookDao().insertPdfFile(bookDownload)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(object :CompletableObserver{
                                        override fun onSubscribe(d: Disposable) {
                                            Log.d("TAG", "onSubscribe: insertPdf")
                                        }

                                        override fun onComplete() {
                                            Log.d("TAG", "onComplete: insertPdf")
                                        }

                                        override fun onError(e: Throwable) {
                                            Log.d("TAG", "insertPdf onError: $e")
                                        }

                                    })

                                progressBar_download.visibility = View.GONE
                                numberDownload.visibility = View.GONE

                                Toast.makeText(
                                    requireContext(),
                                    "Download done",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        outputStream.write(buffer, 0, byte)
                        byte = inputStream.read(buffer)

                    }
                    outputStream.close()
                    inputStream.close()

                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Not Successful, can't download this book",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

            }
        } catch (e: Exception) {
            Log.d("TAG", "error startDownloading: $e")
        }
    }

}
