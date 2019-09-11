package com.kartinimedia.albumcantik.adapter

import com.kartinimedia.albumcantik.model.Slider
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder

class PageAdapter(private val datas: List<Slider?>?): SliderAdapter() {
    override fun getItemCount(): Int = datas?.size as Int

    override fun onBindImageSlide(position: Int, imageSlideViewHolder: ImageSlideViewHolder?) {
        val data = datas?.get(position)
        imageSlideViewHolder?.bindImageSlide(data?.image)
    }

}