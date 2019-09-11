package com.kartinimedia.albumcantik.adapter

import com.kartinimedia.albumcantik.model.Promo
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class PromoAdapter(private val datas: List<Promo>): SliderAdapter() {
    override fun getItemCount(): Int = datas.size

    override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
        val data = datas[position]
        imageSlideViewHolder?.bindImageSlide(data.gambar)
    }

}