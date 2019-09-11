package com.kartinimedia.albumcantik.activity

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.adapter.TransferAdapter
import com.kartinimedia.albumcantik.model.TransferData
import com.kartinimedia.albumcantik.utils.Const
import com.kartinimedia.albumcantik.utils.ItemDecorator
import id.voela.iduangs.IDUang
import kotlinx.android.synthetic.main.activity_done_order.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DoneOrderActivity : AppCompatActivity() {

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done_order)

        val dataBank = intent.getStringArrayListExtra(Const.dataBank)
        val dataRekening = intent.getStringArrayListExtra(Const.dataRekening)
        val hargaUnik = intent.getIntExtra(Const.hargaUnik, 0)
        val orderId = intent.getIntExtra(Const.id, 0)

        val dataTransferBank = ArrayList<TransferData>()

        var index = 0
        dataRekening.forEach {
            dataTransferBank.add(TransferData(dataBank[index], it))
            index = index.plus(1)
        }

        val transferAdapter = TransferAdapter(dataTransferBank)

        rv_rekening.apply {
            adapter = transferAdapter
            layoutManager = LinearLayoutManager(this@DoneOrderActivity)
            addItemDecoration(ItemDecorator(32))
        }

        btn_kembali.onClick {
            startActivity<MainActivity>()
            finish()
        }

        tv_harga_unik.text = IDUang.parsingRupiah(hargaUnik)

        btn_konfirmasi_done_order.onClick {
            Permissions.check(this@DoneOrderActivity, permissions, null, null, object: PermissionHandler() {
                override fun onGranted() {
                    startActivity<KonfirmasiActivity>(
                        Const.id to orderId
                    )
                    finish()
                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    toast("Izin akses ditolak")
                }
            })
        }
    }
}
