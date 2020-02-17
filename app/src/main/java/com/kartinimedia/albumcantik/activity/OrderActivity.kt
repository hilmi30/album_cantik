package com.kartinimedia.albumcantik.activity

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.kaopiz.kprogresshud.KProgressHUD
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.presenter.OrderPresenter
import com.kartinimedia.albumcantik.utils.*
import com.kartinimedia.albumcantik.view.OrderView
import com.travijuu.numberpicker.library.Interface.ValueChangedListener
import id.voela.iduangs.IDUang
import kotlinx.android.synthetic.main.activity_order.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import android.widget.ArrayAdapter
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.kartinimedia.albumcantik.model.DataAlamat
import com.kartinimedia.albumcantik.model.DataOrder
import com.kartinimedia.albumcantik.model.DataPayment
import com.squareup.picasso.Picasso
import org.jetbrains.anko.okButton
import org.jetbrains.anko.toast
import kotlin.collections.ArrayList


class OrderActivity : AppCompatActivity(), OrderView {

    private val presenter = OrderPresenter()
    private lateinit var loading: KProgressHUD
    private var namaProduct = ""
    private var harga = 0
    private var hargaReseller = 0
    private var idPayment = 0
    private var image = ""
    private var alamatStatus = false
    private var isDetailOrder = true
    private var provId = 0
    private var kabId = 0
    private var kecId = 0
    private var productId = 0
    private var jumlahOrder = 1
    private var kodeOrder = ""
    private var statusPembayaran = ""
    private var metodePembayaran = ""
    private var tglOrder = ""
    private var statusOrder = ""
    private val dataBank = ArrayList<String>()
    private val dataRekening = ArrayList<String>()
    private var hargaUnik = 0
    private var orderId = 0
    private var statusLunas = 0
    private var orderNote = ""
    private var orderProv = ""
    private var orderKab = ""
    private var orderKec = ""
    private var kodePos = 0
    private var orderAddress = ""
    private var orderPhone = ""
    private var orderEmail = ""
    private var atasNama = ""
    private var totalHarga = 0

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        onAttachView()
    }

    override fun onAttachView() {
        presenter.onAttach(this)

        setSupportActionBar(toolbar_order)
        supportActionBar?.apply {
            title = getString(R.string.order)
            setDisplayHomeAsUpEnabled(true)
        }

        loading = kProgressHUD(this, getString(R.string.mohon_tunggu))

        presenter.getPayment(this)

        btn_ganti_alamat.onClick {
            startActivity<AlamatActivity>()
        }

        namaProduct = intent.getStringExtra(Const.namaProduct)
        harga = intent.getIntExtra(Const.harga, 0)
        hargaReseller = intent.getIntExtra(Const.hargaReseller, 0)
        image = intent.getStringExtra(Const.image)
        productId = intent.getIntExtra(Const.productId, 0)
        isDetailOrder = intent.getBooleanExtra(Const.isDetailOrder, true)

        tv_nama_product_order.text = namaProduct

        when (checkRole(this)) {
            0 -> tv_harga.text = IDUang.parsingRupiah(harga)
            1 -> tv_harga.text = IDUang.parsingRupiah(hargaReseller)
        }

        if (isDetailOrder) {
            // jika detail order
            orderId = intent.getIntExtra(Const.id, 0)
            jumlahOrder = intent.getIntExtra(Const.jumlahOrder, 1)
            kodeOrder = intent.getStringExtra(Const.kodeOrder)
            tglOrder = intent.getStringExtra(Const.tglOrder)
            statusOrder = intent.getStringExtra(Const.statusOrder)
            statusPembayaran = intent.getStringExtra(Const.statusPembayaran)
            metodePembayaran = intent.getStringExtra(Const.metodePembayaran)
            statusLunas = intent.getIntExtra(Const.statusLunas, 0)
            orderNote = intent.getStringExtra(Const.orderNote)
            orderProv = intent.getStringExtra(Const.prov)
            orderKab = intent.getStringExtra(Const.kab)
            orderKec = intent.getStringExtra(Const.kec)
            kodePos = intent.getIntExtra(Const.kodePos, 0)
            orderAddress = intent.getStringExtra(Const.alamat)
            atasNama = intent.getStringExtra(Const.atasNama)
            orderPhone = intent.getStringExtra(Const.noHp)
            orderEmail = intent.getStringExtra(Const.email)
            hargaUnik = intent.getStringExtra(Const.kodePembayaran).toInt()

            tv_title_pilih_pembayaran.hilang()
            spinner_payment.hilang()
            btn_ganti_alamat.hilang()
            number_pick.hilang()
            lyt_info_order.terlihat()
            btn_batal_pesan.terlihat()
            tv_harga_belum_unik.hilang()

            number_pick.value = jumlahOrder
            tv_kode_pembayaran.text = hargaUnik.toString()
            tv_atas_nama_order.text = atasNama
            tv_no_telp_order.text = orderPhone
            tv_alamat_order.text = orderAddress
            tv_provinsi_order.text = orderProv
            tv_kabupaten_order.text = orderKab
            tv_kecamatan_order.text = orderKec
            tv_kode_pos_order.text = kodePos.toString()

            when (checkRole(this)) {
                0 -> {
                    totalHarga = harga * jumlahOrder
                    tv_total_harga.text = IDUang.parsingRupiah(totalHarga + hargaUnik)
                }
                1 -> {
                    totalHarga = hargaReseller * jumlahOrder
                    tv_total_harga.text = IDUang.parsingRupiah(totalHarga + hargaUnik)
                }
            }

            edt_note_order.isEnabled = false

            if (statusLunas == 0) btn_cara_pembayaran.terlihat() else btn_cara_pembayaran.hilang()
            if (statusLunas == 2) btn_batal_pesan.hilang() else btn_batal_pesan.terlihat()

            tv_kode_detail_order.text = kodeOrder
            tv_tgl_detail_order.text = tglOrder
            tv_status_detail_order.text = statusOrder
            tv_status_pembayaran_detail_order.text = statusPembayaran
            tv_metode_pembayaran_detail_order.text = metodePembayaran
            tv_jumlah_detail_order.text = jumlahOrder.toString()
            edt_note_order.setText(orderNote)
            tv_jumlah_order.text = jumlahOrder.toString()

        } else {
            // jika bukan detail order
            presenter.detailAlamat(this, getAlamatUtama(this))

            tv_title_pilih_pembayaran.terlihat()
            spinner_payment.terlihat()
            btn_ganti_alamat.terlihat()
            lyt_info_order.hilang()
            btn_cara_pembayaran.hilang()
            btn_batal_pesan.hilang()
            lyt_kode_unik.hilang()
            edt_note_order.isEnabled = true
            lyt_jumlah_order.hilang()

            when (checkRole(this)) {
                0 -> tv_total_harga.text = IDUang.parsingRupiah(harga)
                1 -> tv_total_harga.text = IDUang.parsingRupiah(hargaReseller)
            }
        }

        Picasso.get()
            .load(image)
            .placeholder(R.drawable.placeholder)
            .fit()
            .centerCrop()
            .error(R.drawable.placeholder)
            .into(img_product_order)

        number_pick.valueChangedListener = ValueChangedListener { value, _ ->

            jumlahOrder = value

            when (checkRole(this)) {
                0 -> tv_total_harga.text = IDUang.parsingRupiah(harga * value)
                1 -> tv_total_harga.text = IDUang.parsingRupiah(hargaReseller * value)
            }
        }

        btn_ganti_alamat.onClick {
            startActivity<AlamatActivity>()
        }

        btn_pilih_alamat.onClick {
            startActivity<AlamatActivity>()
        }

        btn_cara_pembayaran.onClick {
            Permissions.check(this@OrderActivity, permissions, null, null, object: PermissionHandler() {
                override fun onGranted() {
                    startActivity<DoneOrderActivity>(
                        Const.dataBank to dataBank,
                        Const.dataRekening to dataRekening,
                        Const.hargaUnik to hargaUnik,
                        Const.id to orderId,
                        Const.totalHarga to totalHarga
                    )
                    finish()
                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    toast("Izin akses ditolak")
                }
            })
        }

        btn_batal_pesan.onClick {
            alert {
                isCancelable = false
                message = getString(R.string.yakin_batal_pesan)
                positiveButton(R.string.ya) {
                    presenter.deleteOrder(this@OrderActivity, orderId)
                }
                negativeButton(getString(R.string.tidak)) {
                    it.dismiss()
                }
            }.show()
        }
    }

    override fun showLoading() {
        loading.show()
        lyt_tidak_ada_alamat.hilang()
    }

    override fun showAlamat(data: DataAlamat) {
        alamatStatus = true
        lyt_alamat.terlihat()
        tv_atas_nama_order.text = data.atasNama
        tv_no_telp_order.text = data.nomerHp
        tv_alamat_order.text = data.alamat
        tv_provinsi_order.text = data.provinsi
        tv_kabupaten_order.text = data.kabupaten
        tv_kecamatan_order.text = data.kecamatan
        tv_kode_pos_order.text = data.kodePos.toString()

        provId = data.idProv
        kabId = data.idKab
        kecId = data.idKec
    }

    override fun alamatKosong() {
        alamatStatus = false
        lyt_tidak_ada_alamat.terlihat()
        lyt_alamat.hilang()
    }

    override fun error(msg: String) {
        alamatStatus = false
        alert {
            isCancelable = false
            message = msg
            positiveButton(R.string.coba_lagi) {
                presenter.detailAlamat(this@OrderActivity, getAlamatUtama(this@OrderActivity))
            }
            negativeButton(R.string.batal) {
                it.dismiss()
            }
        }
    }

    override fun hideLoading() {
        loading.dismiss()
    }

    override fun suksesOrder(dataOrder: DataOrder) {
        startActivity<DoneOrderActivity>(
            Const.dataBank to dataBank,
            Const.dataRekening to dataRekening,
            Const.hargaUnik to hargaUnik,
            Const.id to dataOrder.id,
            Const.totalHarga to totalHarga
        )
        finish()
    }

    override fun showPayment(dataPayment: List<DataPayment>) {

        dataBank.clear()
        dataRekening.clear()

        dataPayment[0].listPayment.forEach {
            dataBank.add(it.paymentMethod)
            dataRekening.add(it.nomerRekening)
        }

        val data = ArrayList<String>()
        val idData = ArrayList<Int>()

        dataPayment.forEach {
            data.add(it.paymentMethod)
            idData.add(it.id)
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, data
        )

        spinner_payment.adapter = adapter
        spinner_payment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                idPayment = idData[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                //
            }
        }
    }

    override fun suksesHapusOrder() {
        alert {
            isCancelable = false
            message = getString(R.string.pesanan_berhasil_batal)
            okButton {
                onBackPressed()
            }
        }.show()
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
        if (!isDetailOrder) menuInflater.inflate(R.menu.menu_order, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.kirim_order -> {
                if (!alamatStatus || idPayment == 0) {
                    toast(getString(R.string.mohon_lengkapi))
                } else {

                    alert {
                        isCancelable = false
                        message = getString(R.string.yakin_kirim_order)
                        positiveButton(R.string.ya) {
                            hargaUnik = (100 until 999).random()

                            when (checkRole(this@OrderActivity)) {
                                0 -> totalHarga = (harga * jumlahOrder)
                                1 -> totalHarga = (hargaReseller * jumlahOrder)
                            }

                            presenter.addOrder(
                                context = this@OrderActivity,
                                jumlahOrder = number_pick.value,
                                paymentId = idPayment,
                                orderName = tv_atas_nama_order.text.toString(),
                                orderEmail = getEmail(this@OrderActivity),
                                orderPhone = tv_no_telp_order.text.toString(),
                                provId = provId,
                                kabId = kabId,
                                kecId = kecId,
                                productId = productId,
                                kodePos = tv_kode_pos_order.text.toString().toInt(),
                                orderAddress = tv_alamat_order.text.toString(),
                                orderNote = edt_note_order.text.toString(),
                                kodeUnik = hargaUnik
                            )
                        }
                        negativeButton(R.string.batal) {
                            it.dismiss()
                        }
                    }.show()
                }
            }
            else -> onBackPressed()
        }
        return true
    }

    override fun onRestart() {
        super.onRestart()
        presenter.detailAlamat(this, getAlamatUtama(this))
        presenter.getPayment(this)
    }
}
