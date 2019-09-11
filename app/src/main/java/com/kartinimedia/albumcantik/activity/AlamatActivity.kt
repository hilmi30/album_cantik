package com.kartinimedia.albumcantik.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaopiz.kprogresshud.KProgressHUD
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.adapter.AlamatAdapter
import com.kartinimedia.albumcantik.model.DataAlamat
import com.kartinimedia.albumcantik.presenter.AlamatPresenter
import com.kartinimedia.albumcantik.utils.*
import com.kartinimedia.albumcantik.view.AlamatView
import kotlinx.android.synthetic.main.activity_alamat.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity

class AlamatActivity : AppCompatActivity(), AlamatView {

    private val presenter = AlamatPresenter()
    private lateinit var loading: KProgressHUD
    private val alamatData: HashMap<Int, DataAlamat> = hashMapOf()
    private lateinit var alamatAdapter: AlamatAdapter
    private var alamatSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alamat)
        onAttachView()
    }

    override fun showLoading() {
        loading.show()
        tv_alamat_kosong.hilang()
    }

    override fun hideLoading() {
        loading.dismiss()
    }

    override fun error(msg: String) {
        alert {
            message = msg
            positiveButton(getString(R.string.coba_lagi)) {
                presenter.getAlamat(this@AlamatActivity)
            }
            negativeButton(getString(R.string.keluar)) {
                it.dismiss()
            }
        }.show()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        loading = kProgressHUD(this, getString(R.string.mohon_tunggu))

        setSupportActionBar(toolbar_alamat)
        supportActionBar?.apply {
            title = "Alamat"
            setDisplayHomeAsUpEnabled(true)
        }

        presenter.getAlamat(this)

        setAlamatAdapter(0)
    }

    private fun setAlamatAdapter(reload: Int) {
        alamatAdapter = AlamatAdapter(alamatData, getAlamatUtama(this)) {
            startActivity<FormAlamatActivity>(
                Const.id to it.id,
                Const.alamat to it.alamat,
                Const.kodePos to it.kodePos,
                Const.atasNama to it.atasNama,
                Const.noHp to it.nomerHp,
                Const.prov to it.provinsi,
                Const.kab to it.kabupaten,
                Const.kec to it.kecamatan,
                Const.status to true,
                Const.dataSize to alamatSize,
                Const.idProv to it.idProv,
                Const.idKab to it.idKab,
                Const.idKec to it.idKec
            )
        }

        rv_alamat.apply {
            layoutManager = LinearLayoutManager(this@AlamatActivity)
            adapter = alamatAdapter

            when (reload) {
                0 -> addItemDecoration(ItemDecorator(32))
            }
        }
    }

    override fun alamatKosong() {
        tv_alamat_kosong.terlihat()
    }

    override fun showData(data: List<DataAlamat>) {
        alamatData.clear()
        var index = 0
        data.forEach {
            if (it.id == getAlamatUtama(this)) {
                alamatData[0] = it
            } else {
                index = index.plus(1)
                alamatData[index] = it
            }
        }
        alamatData.toSortedMap()
        alamatAdapter.notifyDataSetChanged()

        alamatSize = alamatData.size
    }

    override fun onDetachView() {
        presenter.onDetach()
        presenter.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_alamat, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.tambah_alamat -> {
                startActivity<FormAlamatActivity>(
                    Const.status to false,
                    Const.dataSize to alamatSize
                )
            }
            else -> onBackPressed()
        }
        return true
    }

    override fun onRestart() {
        super.onRestart()
        presenter.getAlamat(this)
        setAlamatAdapter(1)
    }
}
