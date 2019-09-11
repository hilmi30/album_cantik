package com.kartinimedia.albumcantik.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.kaopiz.kprogresshud.KProgressHUD
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.adapter.ProductAdapter
import com.kartinimedia.albumcantik.model.DataProduct
import com.kartinimedia.albumcantik.presenter.ProductPresenter
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.activity_product.*
import org.jetbrains.anko.alert
import com.kartinimedia.albumcantik.utils.*
import com.kartinimedia.albumcantik.view.ProductView
import id.voela.iduangs.IDUang
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity


class ProductActivity : AppCompatActivity(), ProductView {

    private var startPage = 1
    private var pageCount = 0
    private val productData: MutableList<DataProduct> = mutableListOf()
    private lateinit var productAdapter: ProductAdapter
    private val presenter = ProductPresenter()
    private var kategoriId = 0
    private lateinit var loading: KProgressHUD
    private var itemIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        onAttachView()
    }

    override fun showLoading() {
        loading.show()
        lyt_product.hilang()
    }

    override fun hideLoading() {
        loading.dismiss()
        lyt_product.terlihat()
    }

    override fun showData(data: List<DataProduct>) {
        productData.addAll(data)
        productAdapter.notifyDataSetChanged()
        dots.noOfPages = data.size
    }

    override fun resetData(data: List<DataProduct>) {
        productData.clear()
        productData.addAll(data)
        productAdapter.notifyDataSetChanged()
        dots.noOfPages = data.size
    }

    override fun error(msg: String) {
        alert {
            message = msg
            positiveButton(getString(R.string.coba_lagi)) {
                startPage = 0
                presenter.getProduct(this@ProductActivity, kategoriId, startPage)
            }
            negativeButton(getString(R.string.keluar)) {
                it.dismiss()
            }
        }.show()
    }

    override fun onAttachView() {
        presenter.onAttach(this)

        kategoriId = intent.getIntExtra(Const.kategoriId, 0)
        val namaKategori = intent.getStringExtra(Const.kategoriNama)

        setSupportActionBar(toolbar_product)
        supportActionBar?.apply {
            title = namaKategori
            setDisplayHomeAsUpEnabled(true)
        }

        loading = kProgressHUD(this, getString(R.string.mohon_tunggu))

        presenter.getProduct(this, kategoriId, startPage)

        productAdapter = ProductAdapter(productData) {
            goToUpload(it)
        }

        rv_product.apply {
            adapter = productAdapter
            setOrientation(DSVOrientation.HORIZONTAL)
            setItemTransformer(ScaleTransformer.Builder().setMinScale(0.8f).build())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && startPage <= pageCount && pageCount != 1) {
                        startPage = startPage.plus(1)
                        presenter.getProduct(this@ProductActivity, kategoriId, startPage)
                    }
                }
            })
            addOnItemChangedListener { _, i ->
                itemIndex = i
                changeText(productData[i])
                dots.onPageChange(i)
            }
        }

        product_btn_order_sekarang.onClick {
            goToUpload(productData[itemIndex])
        }
    }

    private fun goToUpload(it: DataProduct) {
        loading.show()
        if (getStatus(this@ProductActivity)) {
            startActivity<UploadActivity>(
                Const.totalFoto to it.specification.totalFoto,
                Const.id to it.id,
                Const.isDraft to false,
                Const.harga to it.harga,
                Const.hargaReseller to it.hargaReseller,
                Const.namaProduct to it.namaProduk,
                Const.image to it.foto
            )
        } else {
            startActivity<LoginActivity>()
        }
    }

    private fun changeText(dataItem: DataProduct) {
        tv_nama_product.text = dataItem.namaProduk
        tv_total_halaman.text = dataItem.specification.totalHalaman
        tv_total_foto.text = dataItem.specification.totalFoto.toString()

        when (checkRole(this)) {
            0 -> tv_harga_product.text = IDUang.parsingRupiah(dataItem.harga)
            1 -> tv_harga_product.text = IDUang.parsingRupiah(dataItem.hargaReseller)
        }
    }

    override fun onDetachView() {
        presenter.onDetach()
        presenter.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun setPage(totalPage: Int?) {
        pageCount = totalPage as Int
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRestart() {
        super.onRestart()
        loading.dismiss()
    }
}
