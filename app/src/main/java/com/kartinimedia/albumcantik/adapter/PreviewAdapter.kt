package com.kartinimedia.albumcantik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.recyclerview.widget.RecyclerView
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.utils.hilang
import com.kartinimedia.albumcantik.utils.terlihat
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_preview.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class PreviewAdapter(
    private val length: Int,
    private val datas: HashMap<Int, String>,
    private val listener: (Int) -> Unit
)
    : RecyclerView.Adapter<PreviewAdapter.ViewHolder>() {

    private var index = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_preview,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = length

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(datas[position])

        holder.itemView.tv_page_count.text = "${position.plus(1)}"
        holder.itemView.onClick {
            index = position
            notifyDataSetChanged()
        }

        holder.itemView.btn_delete.onClick {
            listener(position)
        }

        if (index == position) {
            holder.itemView.img_preview.setBackgroundResource(R.drawable.border)

            if (datas[position].isNullOrEmpty()) {
                holder.itemView.lyt_delete.hilang()
            } else {
                holder.itemView.lyt_delete.terlihat()
            }
        } else {
            holder.itemView.img_preview.setBackgroundResource(R.drawable.nothing)
            holder.itemView.lyt_delete.hilang()
        }
    }

    fun indexPlusOne() {
        if (index < length.minus(1)) {
            index = index.plus(1)
            notifyDataSetChanged()
        }
    }

    fun getIndex(): Int = index

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(data: String?) {

            var path = ""
            if (!URLUtil.isValidUrl(data)) path = "file://"

            Picasso.get()
                .load(path + data)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(v.img_preview)
        }
    }
}