package com.kartinimedia.albumcantik.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.model.GalleryModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_gallery.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class GalleryAdapter(
    private val datas: List<GalleryModel>,
    private val listener: (Int, GalleryModel) -> Unit
)
    : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_gallery,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(datas[position], listener, position)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        @SuppressLint("SetTextI18n")
        fun bindItem(data: GalleryModel, listener: (Int, GalleryModel) -> Unit, position: Int) {
            Picasso.get()
                .load("file://${data.allImagePath[0]}")
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(v.img_folder)

            v.tv_folder_size.text = "${data.allImagePath.size} Foto"
            v.tv_folder_title.text = data.strFolder

            itemView.onClick {
                listener(position, data)
            }
        }
    }
}