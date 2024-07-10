
package com.example.libraryofworld.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryofworld.R
import com.example.libraryofworld.api.Book
import com.example.libraryofworld.api.Works
import com.example.libraryofworld.fragment.BookInfoFragment
import com.example.libraryofworld.fragment.KEY
import com.squareup.picasso.Picasso

class AdapterBook(private val bookList: List<Book>?)
    :RecyclerView.Adapter<AdapterBook.BookViewHolder>() {

    var itemOnClick:((Book)->Unit)? = null

    inner class BookViewHolder(item:View):RecyclerView.ViewHolder(item){
        val title :TextView = item.findViewById(R.id.name_bookTV)
        val auther :TextView = item.findViewById(R.id.name_writerTV)
        val imageBook : ImageView = item.findViewById(R.id.image_book)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_style,parent,false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = bookList!![position]

        if (currentBook != null){
            //used format because if no do it will output [name author] cause name from list
            val authorName = currentBook.author_name.joinToString("")
            holder.title.text = currentBook.title
            holder.auther.text =authorName

            currentBook.coverId?.let { coverId ->
                val imageUrl = "https://covers.openlibrary.org/b/id/$coverId-L.jpg"
                Picasso.get().load(imageUrl).into(holder.imageBook)
            }
            holder.itemView.setOnClickListener {
                itemOnClick?.invoke(currentBook)

            }
        }else{
            Log.d("TAG", "onBindViewHolder: bookList is null")
        }
    }

    override fun getItemCount(): Int {
        return bookList!!.size
    }
}
