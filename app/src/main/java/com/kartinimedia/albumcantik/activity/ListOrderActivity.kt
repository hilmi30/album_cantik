package com.kartinimedia.albumcantik.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaopiz.kprogresshud.KProgressHUD
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.adapter.OrderAdapter
import com.kartinimedia.albumcantik.model.DataOrder
import com.kartinimedia.albumcantik.presenter.ListOrderPresenter
import com.kartinimedia.albumcantik.utils.*
import com.kartinimedia.albumcantik.view.ListOrderView
import kotlinx.android.synthetic.main.activity_list_order.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity

class ListOrderActivity : AppCompatActivity(), ListOrderView {

    private val presenter = ListOrderPresenter()
    private lateinit var loading: KProgressHUD
    private val orderData: MutableList<DataOrder> = mutableListOf()
    private lateinit var orderAdapter: OrderAdapter
    private var startPage = 1
    private var pageCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_order)
        onAttachView()
    }

    override fun onAttachView() {
        presenter.onAttach(this)

        setSupportActionBar(toolbar_list_order)
        supportActionBar?.apply {
            title = "Riwayat Order"
            setDisplayHomeAsUpEnabled(true)
        }

        loading = kProgressHUD(this, getString(R.string.mohon_tunggu))

        presenter.getListOrder(this, startPage)

        orderAdapter = OrderAdapter(orderData, this) {
            startActivity<OrderActivity>(
                Const.productId to it.detailOrder.product.id,
                Const.harga to it.detailOrder.product.harga,
                Const.hargaReseller to it.detailOrder.product.hargaReseller,
                Const.namaProduct to it.detailOrder.product.namaProduk,
                Const.image to it.detailOrder.product.foto,
                Const.jumlahOrder to it.jumlahOrder,
                Const.kodeOrder to it.kodeOrder,
                Const.tglOrder to it.tanggalOrder,
                Const.statusOrder to it.statusOrder,
                Const.statusPembayaran to it.lunas,
                Const.metodePembayaran to it.metodePembayaran,
                Const.id to it.id,
                Const.statusLunas to it.statusLunas,
                Const.orderNote to it.orderNote,
                Const.prov to it.orderProv,
                Const.kab to it.orderKab,
                Const.kec to it.orderKec,
                Const.kodePos to it.kodePos,
                Const.atasNama to it.orderName,
                Const.alamat to it.orderAddress,
                Const.noHp to it.orderPhone,
                Const.email to it.orderEmail,
                Const.kodePembayaran to it.kodeUnik,
                // sebagai detail order
                Const.isDetailOrder to true
            )
        }

        rv_order.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(this@ListOrderActivity)
            addItemDecoration(ItemDecorator(32))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && startPage <= pageCount && pageCount != 1) {
                        startPage = startPage.plus(1)
                        presenter.getListOrder(this@ListOrderActivity, startPage)
                    }
                }
            })
        }

        swipe_order.setOnRefreshListener {
            startPage = 1
            presenter.getListOrder(this@ListOrderActivity, startPage)
        }
    }

    override fun orderKosong() {
        tv_order_kosong.terlihat()
    }

    override fun onDetachView() {
        presenter.onDetach()
        presenter.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun showLoading() {
        loading.show()
        tv_order_kosong.hilang()
    }

    override fun error(msg: String) {
        alert {
            isCancelable = false
            message = msg
            positiveButton(R.string.coba_lagi) {
                startPage = 1
                presenter.getListOrder(this@ListOrderActivity, startPage)
            }
            negativeButton(R.string.keluar) {
                it.dismiss()
            }
        }.show()
    }

    override fun hideLoading() {
        loading.dismiss()
    }

    override fun showData(dataOrder: List<DataOrder>) {
        orderData.clear()
        orderData.addAll(dataOrder)
        orderAdapter.notifyDataSetChanged()

        swipe_order.isRefreshing = false
    }

    override fun showCurrentData(dataOrder: List<DataOrder>) {
        orderData.addAll(dataOrder)
        orderAdapter.notifyDataSetChanged()
    }

    override fun setTotalPage(totalPage: Int) {
        pageCount = totalPage
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRestart() {
        super.onRestart()
        startPage = 1
        presenter.getListOrder(this, startPage)
    }
}
