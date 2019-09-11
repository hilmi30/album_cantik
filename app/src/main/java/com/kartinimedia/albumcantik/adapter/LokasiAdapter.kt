package com.kartinimedia.albumcantik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.model.DataLokasi
import kotlinx.android.synthetic.main.item_lokasi.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class LokasiAdapter(private val datas: List<DataLokasi>, private val tingkat: Int, private val listener: (DataLokasi) -> Unit)
    : RecyclerView.Adapter<LokasiAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_lokasi,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.bindItem(data, listener, tingkat)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(
            data: DataLokasi,
            listener: (DataLokasi) -> Unit,
            tingkat: Int
        ) {

            when (tingkat) {
                1 -> v.tv_lokasi.text = data.nama
                2 -> v.tv_lokasi.text = data.kabupaten
                3 -> v.tv_lokasi.text = data.kecamatan
            }

            itemView.onClick {
                listener(data)
            }
        }
    }
}