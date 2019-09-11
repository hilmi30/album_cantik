package com.kartinimedia.albumcantik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.model.GalleryModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_photo.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class PhotoAdapter(
    private val datas: List<GalleryModel>,
    private val pos: Int,
    private val listener: (String) -> Unit
)
    : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_photo,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = datas[pos].allImagePath.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(datas[pos], listener, position)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(data: GalleryModel, listener: (String) -> Unit, position: Int) {

            Picasso.get()
                .load("file://${data.allImagePath[position]}")
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(v.img_photo)

            itemView.onClick {
                listener(data.allImagePath[position])
            }
        }
    }
}