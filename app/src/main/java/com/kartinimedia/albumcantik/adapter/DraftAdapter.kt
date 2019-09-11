package com.kartinimedia.albumcantik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.model.DataProduct
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_draft.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class DraftAdapter(private val datas: List<DataProduct>, private val listener: (DataProduct, String) -> Unit)
    : RecyclerView.Adapter<DraftAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_draft,
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
            data: DataProduct,
            listener: (DataProduct, String) -> Unit
        ) {
            Picasso.get()
                .load(data.foto)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(v.img_draft)
            v.title_draft.text = data.namaProduk

            v.hapus_draft.onClick {
                listener(data, "h")
            }

            itemView.onClick {
                listener(data, "d")
            }
        }
    }
}