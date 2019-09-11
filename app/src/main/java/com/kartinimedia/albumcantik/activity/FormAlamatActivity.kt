package com.kartinimedia.albumcantik.activity

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaopiz.kprogresshud.KProgressHUD
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.adapter.LokasiAdapter
import com.kartinimedia.albumcantik.model.DataAlamat
import com.kartinimedia.albumcantik.model.DataLokasi
import com.kartinimedia.albumcantik.presenter.FormAlamatPresenter
import com.kartinimedia.albumcantik.utils.*
import com.kartinimedia.albumcantik.view.FormAlamatView
import kotlinx.android.synthetic.main.activity_form_alamat.*
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick

class FormAlamatActivity : AppCompatActivity(), FormAlamatView {

    private val presenter = FormAlamatPresenter()
    private val provinsi: MutableList<DataLokasi> = mutableListOf()
    private val kabupaten: MutableList<DataLokasi> = mutableListOf()
    private val kecamatan: MutableList<DataLokasi> = mutableListOf()
    private lateinit var alertDialog: DialogInterface
    private lateinit var provinsiAdapter: LokasiAdapter
    private lateinit var kabupatenAdapter: LokasiAdapter
    private lateinit var kecamatanAdapter: LokasiAdapter
    private lateinit var loading: KProgressHUD
    private var idProv: Int? = null
    private var idKab: Int? = null
    private var idKec: Int? = null
    private var status = false
    private var alamatId = 0
    private var dataSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_alamat)
        onAttachView()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        status = intent.getBooleanExtra(Const.status, false)

        setSupportActionBar(toolbar_tambah_alamat)
        supportActionBar?.apply {
            title = if (status) "Ubah Alamat" else "Tambah Alamat"
            setDisplayHomeAsUpEnabled(true)
        }

        loading = kProgressHUD(this, getString(R.string.mohon_tunggu))
        dataSize = intent.getIntExtra(Const.dataSize, 0)

        checkStatus()

        presenter.getProvinsi(this@FormAlamatActivity)

        btn_provinsi.onClick {
            setAlert(getString(R.string.pilih_provinsi), provinsiAdapter)
        }

        btn_kabupaten.onClick {
            setAlert(getString(R.string.pilih_kabupaten), kabupatenAdapter)
        }

        btn_kecamatan.onClick {
            setAlert(getString(R.string.pilih_kecamatan), kecamatanAdapter)
        }

        provinsiAdapter = LokasiAdapter(provinsi, 1) {
            idProv = it.idProv.toInt()
            btn_provinsi.text = it.nama
            btn_kabupaten.text = getString(R.string.pilih_kabupaten)
            btn_kecamatan.text = getString(R.string.pilih_kecamatan)
            presenter.getKabupaten(this, idProv as Int)
            alertDialog.dismiss()

            // hapus kab dan kec
            kabupaten.clear()
            kabupatenAdapter.notifyDataSetChanged()
            kecamatan.clear()
            kecamatanAdapter.notifyDataSetChanged()
        }

        kabupatenAdapter = LokasiAdapter(kabupaten, 2) {
            idKab = it.idKab.toInt()
            btn_kabupaten.text = it.kabupaten
            btn_kecamatan.text = getString(R.string.pilih_kecamatan)
            presenter.getKecamatan(this, idKab as Int)
            alertDialog.dismiss()

            // hapus kec
            kecamatan.clear()
            kecamatanAdapter.notifyDataSetChanged()
        }

        kecamatanAdapter = LokasiAdapter(kecamatan, 3) {
            idKec = it.idKec.toInt()
            btn_kecamatan.text = it.kecamatan
            alertDialog.dismiss()
        }

        btn_simpan_alamat.onClick {
            if (status) {
                if (formEmpty()) {
                    toast(getString(R.string.mohon_lengkapi_data_alamat))
                    return@onClick
                }

                presenter.updateAlamat(
                    context = this@FormAlamatActivity,
                    alamat = edt_alamat.text.toString(),
                    atasNama = edt_nama.text.toString(),
                    nomerHp = edt_no_telp.text.toString(),
                    kodePos = edt_kode_pos.text.toString(),
                    idKec = idKec,
                    idProv = idProv,
                    idKab = idKab,
                    token = getToken(this@FormAlamatActivity),
                    alamatId = alamatId
                )
            } else {
                if (formEmpty() || locEmpty()) {
                    toast(getString(R.string.mohon_lengkapi_data_alamat))
                    return@onClick
                }

                presenter.addAlamat(
                    context = this@FormAlamatActivity,
                    alamat = edt_alamat.text.toString(),
                    atasNama = edt_nama.text.toString(),
                    nomerHp = edt_no_telp.text.toString(),
                    kodePos = edt_kode_pos.text.toString(),
                    idKec = idKec as Int,
                    idProv = idProv as Int,
                    idKab = idKab as Int,
                    token = getToken(this@FormAlamatActivity)
                )
            }
        }

        btn_hapus_alamat.onClick {
            alert {
                message = "Yakin ingin menghapus alamat ini?"
                positiveButton(getString(R.string.ya)) {
                    presenter.deleteAlamat(this@FormAlamatActivity, getToken(this@FormAlamatActivity), alamatId)
                }
                negativeButton(getString(R.string.batal)) {
                    it.dismiss()
                }
            }.show()
        }

    }

    private fun formEmpty(): Boolean {
        return (edt_alamat.text.isEmpty()
                || edt_nama.text.isEmpty()
                || edt_no_telp.text.isEmpty()
                || edt_kode_pos.text.isEmpty())
    }

    private fun locEmpty(): Boolean {
        return (idProv == null || idKab == null || idKec == null)
    }

    private fun checkStatus() {

        // check jumlah data, jika data kosong maka data alamat pertama wajib alamat utama
        check_alamat_utama.isEnabled = dataSize != 0

        if (status) {

            alamatId = intent.getIntExtra(Const.id, 0)

            val atasNama = intent.getStringExtra(Const.atasNama)
            val alamat = intent.getStringExtra(Const.alamat)
            val kodePos = intent.getIntExtra(Const.kodePos, 0)
            val noHp = intent.getStringExtra(Const.noHp)
            val prov = intent.getStringExtra(Const.prov)
            val kab = intent.getStringExtra(Const.kab)
            val kec = intent.getStringExtra(Const.kec)
            idProv = intent.getIntExtra(Const.idProv, 0)
            idKab = intent.getIntExtra(Const.idKab, 0)
            idKec = intent.getIntExtra(Const.idKec, 0)

            edt_nama.setText(atasNama)
            edt_alamat.setText(alamat)
            edt_kode_pos.setText(kodePos.toString())
            edt_no_telp.setText(noHp)
            btn_provinsi.text = prov
            btn_kabupaten.text = kab
            btn_kecamatan.text = kec

            if (alamatId == getAlamatUtama(this)) {
                check_alamat_utama.isChecked = true
                check_alamat_utama.isEnabled = false
                btn_hapus_alamat.hilang()
            } else {
                check_alamat_utama.isChecked = false
                check_alamat_utama.isEnabled = true
                btn_hapus_alamat.terlihat()
            }

            presenter.getKabupaten(this, idProv as Int)
            presenter.getKecamatan(this, idKab as Int)
        }
    }

    override fun suksesUbahAlamat(data: DataAlamat) {
        updateAlamatUtama(data.id)
    }

    override fun sukseSimpanAlamat(data: DataAlamat) {
        updateAlamatUtama(data.id)
    }

    private fun updateAlamatUtama(id: Int) {
        if (check_alamat_utama.isChecked) {

            getSharedPreferences(Const.userPref, Context.MODE_PRIVATE).edit().apply {
                putInt(Const.alamat, id)
                apply()
            }

            presenter.updateProfile(
                context = this@FormAlamatActivity,
                fullName = null,
                genderId = null,
                passBaru = null,
                passLama = null,
                passRepeat = null,
                alamatId = id
            )
        } else {
            onBackPressed()
        }
    }

    override fun suksesAlamatUtama() {
        onBackPressed()
    }

    override fun suksesHapusAlamat() {
        onBackPressed()
    }

    override fun showLoading() {
        loading.show()
    }

    override fun hideLoading() {
        loading.dismiss()
    }

    override fun onDetachView() {
        presenter.onDetach()
        presenter.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun showProvinsi(data: List<DataLokasi>) {
        provinsi.clear()
        provinsi.addAll(data)
        provinsiAdapter.notifyDataSetChanged()
    }

    override fun showKabupaten(data: List<DataLokasi>) {
        kabupaten.clear()
        kabupaten.addAll(data)
        kabupatenAdapter.notifyDataSetChanged()
    }

    override fun showKecamatan(data: List<DataLokasi>) {
        kecamatan.clear()
        kecamatan.addAll(data)
        kecamatanAdapter.notifyDataSetChanged()
    }

    private fun setAlert(judul: String, lokasiAdapter: LokasiAdapter) {
        alertDialog = alert {
            isCancelable = false
            title = judul
            customView {
                verticalLayout {
                    recyclerView {
                        adapter = lokasiAdapter
                        layoutManager = LinearLayoutManager(ctx)
                    }
                }
            }
            negativeButton(R.string.batal) {
                it.dismiss()
            }
        }.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun error(msg: String) {
        alert {
            message = msg
            isCancelable = false
            positiveButton(R.string.coba_lagi) {

            }
            negativeButton(R.string.keluar) {
                it.dismiss()
            }
        }
    }
}
