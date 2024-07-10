package com.example.libraryofworld.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryofworld.adapter.AdapterBook
import com.example.libraryofworld.R
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.libraryofworld.mvvm.BookViewModel

const val KEY = "apiKEy"

class HomeFragment : Fragment() {

    private lateinit var progressBarRecentlyBook: ProgressBar
    private lateinit var progressBarVariousBook: ProgressBar
    private lateinit var recentlyAddBookRV: RecyclerView
    private lateinit var variousBookRV: RecyclerView
    private lateinit var recentlyAddBook: TextView
    private lateinit var variousBook: TextView
    private lateinit var bookViewModel: BookViewModel
    private lateinit var transaction: FragmentTransaction
    private lateinit var searchBTN: ImageView
    private lateinit var searchET: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //initialized all view in ui
        initializedUI(view)

        //add book in blocked one and two
        recentlyAddBooks()
        variousBook()

        //search
        try {
            searchBTN.setOnClickListener {
                val searchTerm = searchET.text.toString()

                if (searchTerm.isNotEmpty()) {
                    val searchBook = SearchFragment()
                    val bundle = Bundle()
                    bundle.putString("searchBook", searchTerm)
                    searchBook.arguments = bundle

                    transaction.replace(R.id.frLy, searchBook).addToBackStack(null).commit()
                } else {
                    Toast.makeText(requireContext(), "No name enter...", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.d("TAG", "searchError onViewCreated: $e")
        }

        recentlyAddBook.setOnClickListener {
            val forAllFragment = ForAllFragment()
            val bundle = Bundle()
            bundle.putString(KEY, "recentlyAddBook")
            forAllFragment.arguments = bundle

            transaction.replace(R.id.frLy, forAllFragment).addToBackStack(null).commit()

        }

        variousBook.setOnClickListener {

            val forAllFragment = ForAllFragment()
            val bundle = Bundle()
            bundle.putString(KEY, "variousBook")
            forAllFragment.arguments = bundle

            transaction.replace(R.id.frLy, forAllFragment).addToBackStack(null).commit()
        }

    }

    private fun initializedUI(view: View) {


        recentlyAddBook = view.findViewById(R.id.recentlyAddBook)!!
        progressBarRecentlyBook = view.findViewById(R.id.progressBar)!!
        recentlyAddBookRV = view.findViewById(R.id.recyclerViewOfRecentlyAddBook)!!
        recentlyAddBookRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        variousBook = view.findViewById(R.id.variousBook)!!
        progressBarVariousBook = view.findViewById(R.id.progressBar2)!!
        variousBookRV = view.findViewById(R.id.recyclerViewOfvariousBook)!!
        variousBookRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        bookViewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]

        transaction = activity?.supportFragmentManager?.beginTransaction()!!

        searchBTN = view.findViewById(R.id.searchET_button)!!
        searchET = view.findViewById(R.id.edit_text_search)!!


    }

    @SuppressLint("NotifyDataSetChanged")
    fun recentlyAddBooks() {

        bookViewModel.loadRecentlyAddBook()
        bookViewModel.recentlyAddBook.observe(viewLifecycleOwner) { books ->
            val adapterBook = AdapterBook(books)
            recentlyAddBookRV.adapter = adapterBook
            adapterBook.notifyDataSetChanged()
            adapterBook.itemOnClick = {
                val bookInfoFragment = BookInfoFragment()
                val bundle = Bundle()
                bundle.putSerializable(KEY, it)
                bookInfoFragment.arguments = bundle

                transaction.replace(R.id.frLy, bookInfoFragment).addToBackStack(null).commit()

            }
            progressBarRecentlyBook.visibility = View.INVISIBLE

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun variousBook() {
        bookViewModel.loadVariousBook()
        bookViewModel.variousBook.observe(viewLifecycleOwner) { books ->

            val adapterBook = AdapterBook(books)
            variousBookRV.adapter = adapterBook
            adapterBook.notifyDataSetChanged()

            adapterBook.itemOnClick = {
                val bookInfoFragment = BookInfoFragment()
                val bundle = Bundle()
                bundle.putSerializable(KEY, it)
                bookInfoFragment.arguments = bundle

                transaction.replace(R.id.frLy, bookInfoFragment).addToBackStack(null).commit()

            }
            progressBarVariousBook.visibility = View.INVISIBLE

        }
    }


}
