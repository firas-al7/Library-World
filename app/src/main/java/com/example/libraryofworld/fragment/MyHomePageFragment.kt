package com.example.libraryofworld.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.libraryofworld.R

class MyHomePageFragment : Fragment() {

    private lateinit var favoriteBookTV :TextView
    private lateinit var downloadBookTV :TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_my_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteBookTV = view.findViewById(R.id.favoriteBookTV_homePage)!!
        downloadBookTV = view.findViewById(R.id.downloadBookTV_homePage)!!

        val transaction = activity?.supportFragmentManager?.beginTransaction()!!

        favoriteBookTV.setOnClickListener {
            val favoritePage = FavoritesBookFragment()
            transaction.replace(R.id.frLy, favoritePage).addToBackStack(null).commit()
        }

        downloadBookTV.setOnClickListener {

            val downloadPage = DownloadedBookFragment()
            transaction.replace(R.id.frLy, downloadPage).addToBackStack(null).commit()
        }

    }

}
