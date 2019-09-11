package com.kartinimedia.albumcantik.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.kaopiz.kprogresshud.KProgressHUD
import com.kartinimedia.albumcantik.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import android.graphics.drawable.LayerDrawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kartinimedia.albumcantik.adapter.KategoriAdapter
import com.kartinimedia.albumcantik.adapter.PromoAdapter
import com.kartinimedia.albumcantik.model.DataKategori
import com.kartinimedia.albumcantik.model.DataProduct
import com.kartinimedia.albumcantik.model.Promo
import com.kartinimedia.albumcantik.presenter.MainPresenter
import com.kartinimedia.albumcantik.utils.*
import com.kartinimedia.albumcantik.view.MainView
import ss.com.bannerslider.Slider


class MainActivity : AppCompatActivity(), MainView {

    private val presenter = MainPresenter()
    private val kategoriData: MutableList<DataKategori> = mutableListOf()
    private val promoData: MutableList<Promo> = mutableListOf()
    private lateinit var adapterKategori: KategoriAdapter
    private lateinit var loading: KProgressHUD
    private var draftCount = 0
    private var startPage = 1
    private var pageCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onAttachView()
    }

    override fun showLoading() {
        loading.show()
    }

    override fun hideLoading() {
        loading.dismiss()
    }

    override fun error(msg: String) {
        alert {
            isCancelable = false
            message = msg
            positiveButton(getString(R.string.coba_lagi)) {
                startPage = 1
                getAllData()
            }
            negativeButton(getString(R.string.keluar)) {
                it.dismiss()
            }
        }.show()
    }

    override fun showData(it: List<DataKategori>) {
        tv_pilih_kategori.terlihat()
        kategoriData.clear()
        kategoriData.addAll(it)
        adapterKategori.notifyDataSetChanged()
    }

    override fun showCurrentData(data: List<DataKategori>) {
        kategoriData.addAll(data)
        adapterKategori.notifyDataSetChanged()
    }

    override fun onAttachView() {
        presenter.onAttach(this)

        setSupportActionBar(toolbar_main)
        supportActionBar?.title = ""

        loading = kProgressHUD(this, getString(R.string.mohon_tunggu))

        adapterKategori = KategoriAdapter(kategoriData) {
            startActivity<ProductActivity>(
                Const.kategoriId to it.id,
                Const.kategoriNama to it.namaKategori
            )
        }

        rv_kategori.apply {
            adapter = adapterKategori
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            addItemDecoration(ItemDecoratorGrid(24))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && startPage <= pageCount && pageCount != 1) {
                        startPage = startPage.plus(1)
                        presenter.getAllCategory(this@MainActivity, startPage)
                    }
                }
            })
        }

        // get all data in main
        getAllData()
    }

    private fun getAllData() {
        presenter.getAllCategory(this, startPage)
        presenter.getAllDraft(this)
        presenter.getPromo()
    }

    override fun setTotalPage(totalPage: Int) {
        pageCount = totalPage
    }

    override fun onDetachView() {
        presenter.onDetach()
        presenter.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun showPromo(data: List<Promo>) {
        promoData.clear()
        promoData.addAll(data)
        showSliderPromo()
    }

    private fun showSliderPromo() {
        Slider.init(PicassoImageService2())
        main_banner.apply {
            setAdapter(PromoAdapter(promoData))
        }

        main_banner.setOnSlideClickListener {
            val data = promoData[it]
            startActivity<PromoDetailActivity>(
                Const.title to data.caption,
                Const.desc to data.deskripsi,
                Const.image to data.gambar
            )
        }
    }

    override fun promoKosong() {
        //
    }

    override fun promoError() {
        //
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        val menuItem = menu?.findItem(R.id.draft)
        val icon = menuItem?.icon as LayerDrawable

        val badge: CountDrawable

        // Reuse drawable if possible
        val reuse = icon.findDrawableByLayerId(R.id.ic_draft_count)
        badge = if (reuse != null && reuse is CountDrawable) {
            reuse
        } else {
            CountDrawable(this)
        }

        badge.setCount(draftCount.toString())
        icon.mutate()
        icon.setDrawableByLayerId(R.id.ic_draft_count, badge)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (getStatus(this)) {
            when (item?.itemId) {
                R.id.account -> startActivity<AccountActivity>()
                R.id.draft -> startActivity<DraftActivity>()
                R.id.order -> startActivity<ListOrderActivity>()
            }
        } else {
            startActivity<LoginActivity>()
        }

        return true
    }

    override fun showDraft(drafts: List<DataProduct>) {
        draftCount = drafts.size
        invalidateOptionsMenu()
    }

    override fun draftKosong() {
        draftCount = 0
        invalidateOptionsMenu()
    }

    override fun onRestart() {
        super.onRestart()
        presenter.getAllDraft(this)
    }
}
