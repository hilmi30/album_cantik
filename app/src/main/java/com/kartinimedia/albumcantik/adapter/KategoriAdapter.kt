package com.kartinimedia.albumcantik.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.model.DataKategori
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_kategori.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class KategoriAdapter(private val datas: List<DataKategori>, private val listener: (DataKategori) -> Unit)
    : RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_kategori,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.bindItem(data, listener)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(
            data: DataKategori,
            listener: (DataKategori) -> Unit
        ) {

            Log.e("image", data.image)

            Picasso.get()
                .load(data.image)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(v.img_kategori)
            v.title_kategori.text = data.namaKategori

            itemView.onClick {
                listener(data)
            }
        }
    }
}