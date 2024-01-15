package com.example.moviesapp.adapter

import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.MOVIES_PROVIDER_CONTENT_URI
import com.example.moviesapp.R
import com.example.moviesapp.api.Item
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemAdapter(private val context: Context, private val items: MutableList<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        fun bind(item: Item) {
            tvTitle.text = item.title
            Picasso.get()
                .load(File(item.poster_path))
                .error(R.drawable.popcorn)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]


        holder.itemView.setOnLongClickListener{


            if (item.poster_path.isNotEmpty()){ //puca mi app ako je prazan path jer file ne postoji
                context.contentResolver.delete(
                    ContentUris.withAppendedId(MOVIES_PROVIDER_CONTENT_URI, item.id!! ),
                    null,null
                )
                File(item.poster_path).delete()
            }
            items.removeAt(position)
            notifyDataSetChanged()
            true
        }
        holder.itemView.setOnClickListener(){
//            items.removeAt(position)
//            notifyItemRemoved(position)
//
        }
        holder.bind(item)
    }
}