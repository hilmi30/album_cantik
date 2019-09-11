package com.kartinimedia.albumcantik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.model.DataAlamat
import kotlinx.android.synthetic.main.item_alamat.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class AlamatAdapter(private val datas: HashMap<Int, DataAlamat>, private val alamatUtama: Int, private val listener: (DataAlamat) -> Unit)
    : RecyclerView.Adapter<AlamatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_alamat,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.bindItem(data, listener, alamatUtama)
    }

    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
        fun bindItem(
            data: DataAlamat?,
            listener: (DataAlamat) -> Unit,
            alamatUtama: Int
        ) {

            v.tv_atas_nama.text = data?.atasNama
            v.tv_no_telp.text = data?.nomerHp
            v.tv_alamat.text = data?.alamat
            v.tv_provinsi.text = data?.provinsi?.toUpperCase()
            v.tv_kabupaten.text = data?.kabupaten?.toUpperCase()
            v.tv_kecamatan.text = data?.kecamatan?.toUpperCase()
            v.tv_kode_pos.text = data?.kodePos.toString().toUpperCase()

            v.tv_status.text = if (data?.id == alamatUtama) "(UTAMA)" else ""

            itemView.onClick {
                listener(data as DataAlamat)
            }
        }
    }
}