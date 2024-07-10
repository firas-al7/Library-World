package com.example.libraryofworld.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryofworld.R
import com.example.libraryofworld.room.DownloadBookEntity
import com.example.libraryofworld.room.FavoriteBookEntity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import okhttp3.internal.addHeaderLenient

class AdapterRecyclerViewDownloadBook :
    RecyclerView.Adapter<AdapterRecyclerViewDownloadBook.DownloadBookViewHolder>() {

        var pos : Int= 0
    var bookList = listOf<DownloadBookEntity>()
    var itemOnClickRead: ((DownloadBookEntity) -> Unit)? = null
    var itemOnClickDelete: ((DownloadBookEntity) -> Unit)? = null

    inner class DownloadBookViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val title: TextView = item.findViewById(R.id.textView_name_book_downloaded)
        val imageBook: ImageView = item.findViewById(R.id.imageView_book_downloaded)
        val deleteBTN: Button = item.findViewById(R.id.button_delete_book_downloaded)
        val readNowBTN: Button = item.findViewById(R.id.button_download_read_offline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadBookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_style_download_page, parent, false)
        return DownloadBookViewHolder(view)
    }

    override fun getItemCount() = bookList.size

    override fun onBindViewHolder(holder: DownloadBookViewHolder, position: Int) {
        try {
            val currentBook = bookList[position]

            holder.title.text = currentBook.bookTitle

            val imageUrl = currentBook.imageUrl
            Picasso.get()
                .load(imageUrl)
                .into(holder.imageBook, object : Callback {
                    override fun onSuccess() {
                        Log.d("TAG", "Picasso onSuccess: done the image")
                    }

                    override fun onError(e: Exception?) {
                        Log.e("TAG", "Picasso Error loading image", e)
                    }
                })

            holder.readNowBTN.setOnClickListener {
                itemOnClickRead?.invoke(currentBook)
            }

            holder.deleteBTN.setOnClickListener {
                pos = holder.adapterPosition
                itemOnClickDelete?.invoke(currentBook)

            }
        } catch (e: Exception) {
            Log.d("TAG", "onBindViewHolder: $e")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItemAt(isDeleted: Boolean) {
        if (isDeleted) {
            val mutableList = bookList.toMutableList()
            mutableList.removeAt(pos)
            bookList = mutableList.toList()
            notifyItemRemoved(pos)
            notifyDataSetChanged()
        }
    }


}