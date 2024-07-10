package com.example.libraryofworld.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.libraryofworld.R
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class ReadBookOfflineFragment : Fragment() {

    private lateinit var pdfView: PDFView

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_read_book_offline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            pdfView = view.findViewById(R.id.pdfView)
            val pdfPath = arguments?.getString("KeyPDF")
            val pdfViewUri = File(pdfPath!!)
            pdfView.fromFile(pdfViewUri).load()

    }

}
