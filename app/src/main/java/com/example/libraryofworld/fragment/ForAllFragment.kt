package com.example.libraryofworld.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryofworld.R
import com.example.libraryofworld.adapter.AdapterBookForAll
import com.example.libraryofworld.mvvm.BookViewModel


class ForAllFragment : Fragment() {

    private lateinit var titlePage: TextView
    private lateinit var rv: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var recentlyBookTranslation: String
    private lateinit var variousBookTranslation: String
    private lateinit var mayYouLikeAlso: String
    private lateinit var bookViewModel: BookViewModel
    private lateinit var adapterBook: AdapterBookForAll
    private lateinit var transaction: FragmentTransaction

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_for_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization(view)

        val apiType = arguments?.getString(KEY)

        transaction = requireActivity().supportFragmentManager.beginTransaction()

        when (apiType) {
            "recentlyAddBook" -> {
                titlePage.text = recentlyBookTranslation
                recentlyAddBook()
            }

            "variousBook" -> {
                titlePage.text = variousBookTranslation
                variousBook()
            }

            "mayYouLike" -> {
                titlePage.text = mayYouLikeAlso
                mayYouLike()
            }
        }

    }

    private fun initialization(view: View) {

        titlePage = view.findViewById(R.id.name_of_page)!!
        rv = view.findViewById(R.id.recyclerView_for_all)!!
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        progressBar = view.findViewById(R.id.progressBarForAll)!!

        recentlyBookTranslation = context?.getString(R.string.first_block)!!
        variousBookTranslation = context?.getString(R.string.second_block)!!
        mayYouLikeAlso = context?.getString(R.string.like)!!

        bookViewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun recentlyAddBook(){

        bookViewModel.loadRecentlyAddBookAllList()
        bookViewModel.recentlyAddBookAll.observe(viewLifecycleOwner) { books ->
            adapterBook = AdapterBookForAll(books)
            rv.adapter = adapterBook
            adapterBook.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE

            adapterBook.itemOnClick = {

                val bookInfoFragment = BookInfoFragment()
                val bundle = Bundle()
                bundle.putSerializable(KEY, it)
                bookInfoFragment.arguments = bundle

                transaction.replace(R.id.frLy, bookInfoFragment).addToBackStack(null).commit()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun variousBook(){

        bookViewModel.loadVariousBookAllList()
        bookViewModel.variousBookAll.observe(viewLifecycleOwner) { book ->
            adapterBook = AdapterBookForAll(book)
            rv.adapter = adapterBook
            adapterBook.notifyDataSetChanged()

            adapterBook.itemOnClick = {
                val bookInfoFragment = BookInfoFragment()
                val bundle = Bundle()
                bundle.putSerializable(KEY, it)
                bookInfoFragment.arguments = bundle

                transaction.replace(R.id.frLy, bookInfoFragment).addToBackStack(null).commit()
            }
            progressBar.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun mayYouLike(){
        bookViewModel.loadMayYouLikeAllList()
        bookViewModel.allMayYouLike.observe(viewLifecycleOwner){book->
            adapterBook = AdapterBookForAll(book)
            rv.adapter = adapterBook
            adapterBook.notifyDataSetChanged()
            progressBar.visibility = View.INVISIBLE

            adapterBook.itemOnClick = {
                val bookInfoFragment = BookInfoFragment()
                val bundle = Bundle()
                bundle.putSerializable(KEY, it)
                bookInfoFragment.arguments = bundle

                transaction.replace(R.id.frLy, bookInfoFragment).addToBackStack(null).commit()
            }
        }
    }

}
