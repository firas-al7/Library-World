package com.example.libraryofworld.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryofworld.R
import com.example.libraryofworld.api.Book
import com.example.libraryofworld.fragment.KEY
import com.squareup.picasso.Picasso

class AdapterBookForAll(private var bookList: List<Book>?):
    RecyclerView.Adapter<AdapterBookForAll.InnerBookViewHolder> (){

        var itemOnClick:((Book)->Unit)? = null

        inner class InnerBookViewHolder(item: View):RecyclerView.ViewHolder(item){
            val title : TextView = item.findViewById(R.id.title2)
            val auther : TextView = item.findViewById(R.id.author2)
            val imageBook : ImageView = item.findViewById(R.id.imageViewBookForAll)

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerBookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_style2,parent,false)
        return InnerBookViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: InnerBookViewHolder, position: Int) {
        val currentBook = bookList!![position]

        //used format because if no do it will output [name author]
        val authorName = currentBook.author_name.joinToString("")

        holder.title.text = currentBook.title
        holder.auther.text = authorName

        currentBook.coverId?.let { coverId ->
            val imageUrl = "https://covers.openlibrary.org/b/id/$coverId-L.jpg"
            Picasso.get().load(imageUrl).into(holder.imageBook)
        }

        holder.itemView.setOnClickListener {
            itemOnClick?.invoke(currentBook)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData(){
        bookList = emptyList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return bookList!!.size
    }
}