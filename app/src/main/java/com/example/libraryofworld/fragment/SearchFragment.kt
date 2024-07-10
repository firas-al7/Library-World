package com.example.libraryofworld.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryofworld.R
import com.example.libraryofworld.adapter.AdapterBookForAll
import com.example.libraryofworld.mvvm.BookViewModel

class SearchFragment : Fragment() {
    private lateinit var searchRV: RecyclerView
    private lateinit var pB: ProgressBar
    private lateinit var noFoundTV: TextView
    private lateinit var adapterBook: AdapterBookForAll
    private lateinit var bookViewModel: BookViewModel
    private lateinit var transaction: FragmentTransaction


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        val searchTerm = arguments?.getString("searchBook")

        try {

                bookViewModel.loadSearchBook(searchTerm!!)
                bookViewModel.searchBook.observe(viewLifecycleOwner) { book ->
                    pB.visibility = View.GONE
                     if (book.isEmpty()) {
                            //no book found
                            noFoundTV.visibility = View.VISIBLE
                            searchRV.isGone = true
                        } else {
                            // Books found
                            noFoundTV.visibility = View.GONE
                            searchRV.visibility = View.VISIBLE

                            adapterBook = AdapterBookForAll(book)
                            searchRV.adapter = adapterBook
                            adapterBook.notifyDataSetChanged()
                            adapterBook.itemOnClick = {
                                val bookInfoFragment = BookInfoFragment()
                                val bundle = Bundle()
                                bundle.putSerializable(KEY, it)
                                bookInfoFragment.arguments = bundle

                                transaction.replace(R.id.frLy, bookInfoFragment)
                                    .addToBackStack(null)
                                    .commit()

                            }

                        }
                    }
        } catch (e: Exception) {
            Log.d("TAG", "searchPage onViewCreated: $e")
        }
    }

    private fun init(view: View) {

        noFoundTV = view.findViewById(R.id.noBookFoundTV)!!
        pB = view.findViewById(R.id.progressBar3Search)!!

        searchRV = view.findViewById(R.id.recyclerViewSearch)!!
        searchRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        bookViewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]

        transaction = requireActivity().supportFragmentManager.beginTransaction()


    }

}
