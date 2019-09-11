package com.kartinimedia.albumcantik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.model.TransferData
import kotlinx.android.synthetic.main.item_transfer.view.*

class TransferAdapter(private val datas: List<TransferData>)
    : RecyclerView.Adapter<TransferAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_transfer,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.bindItem(data)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(data: TransferData) {
            v.tv_nama_bank.text = data.namaBank
            v.tv_no_rekening.text = data.noRekening
        }
    }
}