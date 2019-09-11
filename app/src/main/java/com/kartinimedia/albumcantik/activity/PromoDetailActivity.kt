package com.kartinimedia.albumcantik.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.utils.Const
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_promo_detail.*

class PromoDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promo_detail)
        setSupportActionBar(promodetail_toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }

        val image = intent.getStringExtra(Const.image)
        val title = intent.getStringExtra(Const.title)
        val desc = intent.getStringExtra(Const.desc)

        promodetail_tv_title.text = title
        promodetail_tv_desc.text = desc
        Picasso.get().load(image).fit().centerCrop().error(R.drawable.placeholder).placeholder(R.drawable.placeholder)
            .into(promodetail_img)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
