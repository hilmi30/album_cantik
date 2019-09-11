package com.kartinimedia.albumcantik.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.model.DataOrder
import kotlinx.android.synthetic.main.item_list_order.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor

class OrderAdapter(private val datas: List<DataOrder>, private val context: Context, private val listener: (DataOrder) -> Unit)
    : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list_order,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.bindItem(data, listener, context)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(
            data: DataOrder,
            listener: (DataOrder) -> Unit,
            context: Context
        ) {

            v.tv_kode_list_order.text = data.kodeOrder
            v.tv_nama_product_list_order.text = data.detailOrder.product.namaProduk
            v.tv_tgl_list_order.text = data.tanggalOrder
            v.tv_status_list_order.text = data.statusOrder


            when (data.statusLunas) {
                0 -> v.tv_status_pembayaran_list_order.textColor = ContextCompat.getColor(context, R.color.redAccent)
                1 -> v.tv_status_pembayaran_list_order.textColor = ContextCompat.getColor(context, R.color.blue)
                2 -> v.tv_status_pembayaran_list_order.textColor = ContextCompat.getColor(context, R.color.green)
            }

            v.tv_status_pembayaran_list_order.text = data.lunas

            itemView.onClick {
                listener(data)
            }
        }
    }
}