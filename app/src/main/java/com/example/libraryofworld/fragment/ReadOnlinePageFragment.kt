package com.example.libraryofworld.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import com.example.libraryofworld.R

class ReadOnlinePageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_read_online_page, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView :WebView = view.findViewById(R.id.webView)!!

        val bookUrl = arguments?.getString("bookUrl")
        //Not all books can be read so it may be empty
        if (!bookUrl.isNullOrEmpty()){

            webView.settings.javaScriptEnabled = true
            webView.loadUrl(bookUrl)

        }else{
            Toast.makeText(requireContext(), "Can't reader sorry try another one", Toast.LENGTH_SHORT).show()
        }
    }

}
