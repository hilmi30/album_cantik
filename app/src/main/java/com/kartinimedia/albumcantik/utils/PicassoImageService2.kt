package com.kartinimedia.albumcantik.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso
import ss.com.bannerslider.ImageLoadingService

class PicassoImageService2: ImageLoadingService {
    override fun loadImage(url: String?, imageView: ImageView?) {
        Picasso.get().load(url).fit().centerCrop().into(imageView)
    }

    override fun loadImage(resource: Int, imageView: ImageView?) {
        Picasso.get().load(resource).fit().centerCrop().into(imageView)
    }

    override fun loadImage(url: String?, placeHolder: Int, errorDrawable: Int, imageView: ImageView?) {
        Picasso.get().load(url).fit().centerCrop().placeholder(placeHolder).error(placeHolder).into(imageView)
    }

}