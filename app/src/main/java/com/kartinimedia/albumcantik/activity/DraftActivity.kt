package com.kartinimedia.albumcantik.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaopiz.kprogresshud.KProgressHUD
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.adapter.DraftAdapter
import com.kartinimedia.albumcantik.model.DataProduct
import com.kartinimedia.albumcantik.presenter.DraftPresenter
import com.kartinimedia.albumcantik.utils.*
import com.kartinimedia.albumcantik.view.DraftView
import kotlinx.android.synthetic.main.activity_draft.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DraftActivity : AppCompatActivity(), DraftView {

    private val presenter = DraftPresenter()
    private lateinit var loading: KProgressHUD
    private val draftData: MutableList<DataProduct> = mutableListOf()
    private lateinit var draftAdapter: DraftAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft)
    }

    override fun onStart() {
        super.onStart()
        onAttachView()
    }

    override fun showDraft(drafts: List<DataProduct>) {
        draftData.clear()
        draftData.addAll(drafts)
        draftAdapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        loading.show()
        tv_data_kosong.hilang()
    }

    override fun error(msg: String) {
        alert {
            isCancelable = false
            message = msg
            positiveButton(getString(R.string.coba_lagi)) {
                presenter.getAllDraft(this@DraftActivity)
            }
            negativeButton(getString(R.string.keluar)) {
                it.dismiss()
            }
        }.show()
    }

    override fun hideLoading() {
        loading.dismiss()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        loading = kProgressHUD(this, getString(R.string.mohon_tunggu))
        presenter.getAllDraft(this)

        draftAdapter = DraftAdapter(draftData) { draftsItem: DataProduct, s: String ->
            when (s) {
                "d" -> {
                    loading.show()
                    startActivity<UploadActivity>(
                        Const.totalFoto to draftsItem.specification.totalFoto,
                        Const.id to draftsItem.id,
                        Const.isDraft to true,
                        Const.harga to draftsItem.harga,
                        Const.hargaReseller to draftsItem.hargaReseller,
                        Const.namaProduct to draftsItem.namaProduk,
                        Const.image to draftsItem.foto
                    )
                }
                "h" -> {
                    alert {
                        isCancelable = false
                        message = getString(R.string.yakin_hapus_draft)
                        positiveButton(R.string.ya) {
                            presenter.hapusDraft(this@DraftActivity, draftsItem.id)
                        }
                        negativeButton(R.string.batal) {
                            it.dismiss()
                        }
                    }.show()
                }
            }
        }

        rv_draft.apply {
            adapter = draftAdapter
            layoutManager = LinearLayoutManager(this@DraftActivity)
            addItemDecoration(ItemDecorator(32))
        }

        setSupportActionBar(toolbar_draft)
        supportActionBar?.apply {
            title = getString(R.string.draft)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun dataKosong() {
        draftData.clear()
        draftAdapter.notifyDataSetChanged()
        tv_data_kosong.terlihat()
    }

    override fun onDetachView() {
        presenter.onDetach()
        presenter.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRestart() {
        super.onRestart()
        loading.dismiss()
    }

    override fun suksesHapusDraft() {
        presenter.getAllDraft(this)
    }

    override fun onBackPressed() {
        startActivity<MainActivity>()
        finish()
    }
}
