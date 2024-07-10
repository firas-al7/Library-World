package com.example.libraryofworld.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryofworld.R
import com.example.libraryofworld.room.BookDataBase
import com.example.libraryofworld.room.FavoriteBookEntity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class AdapterRecyclerViewFavorite()
    :RecyclerView.Adapter<AdapterRecyclerViewFavorite.InnerViewHolderFavorite>() {

        var pos : Int = 0
        var bookList = listOf<FavoriteBookEntity>()
    var itemOnClickRead:((FavoriteBookEntity)->Unit)? = null
    var itemOnClickDelete:((FavoriteBookEntity)->Unit)? = null
    var itemOnClickDownload:((FavoriteBookEntity)->Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    inner class InnerViewHolderFavorite(item:View):RecyclerView.ViewHolder(item){
        val title : TextView = item.findViewById(R.id.textView_name_book_my_favorite)
        val imageBook : ImageView = item.findViewById(R.id.imageView_book_my_favorite)
        val deleteBTN : Button = item.findViewById(R.id.button_delete_myFavorite)
        val readNowBTN : Button = item.findViewById(R.id.button_read_now_my_favorite)
        val downloadBTN : Button = item.findViewById(R.id.button_download_my_favorite)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolderFavorite {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_style_favorite_page,parent,false)
        return InnerViewHolderFavorite(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: InnerViewHolderFavorite, position: Int) {
        try {
            if (bookList.isNotEmpty() && position < bookList.size) {
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

                holder.downloadBTN.setOnClickListener {
                    itemOnClickDownload?.invoke(currentBook)
                }
                holder.deleteBTN.setOnClickListener {
                    pos = holder.absoluteAdapterPosition
                    itemOnClickDelete?.invoke(currentBook)

                }
            }
        }catch (e:Exception){
            Log.d("TAG", "onBindViewHolder: $e")
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItemAt(isDelete: Boolean) {
        if (isDelete) {
            val mutableList = bookList.toMutableList()
            mutableList.removeAt(pos)
            bookList = mutableList.toList()
            notifyItemRemoved(pos)
            notifyDataSetChanged()
        }
    }


}
