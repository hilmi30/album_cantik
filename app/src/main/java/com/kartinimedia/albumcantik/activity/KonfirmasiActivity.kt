package com.kartinimedia.albumcantik.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kaopiz.kprogresshud.KProgressHUD
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.presenter.KonfirmasiPresenter
import com.kartinimedia.albumcantik.utils.*
import com.kartinimedia.albumcantik.view.KonfirmasiView
import kotlinx.android.synthetic.main.activity_konfirmasi.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.File

class KonfirmasiActivity : AppCompatActivity(), KonfirmasiView {

    private val presenter = KonfirmasiPresenter()
    private var orderId = 0
    private var loading: KProgressHUD? = null
    private var path = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi)
        onAttachView()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
        loading = kProgressHUD(this, getString(R.string.mohon_tunggu))

        orderId = intent.getIntExtra(Const.id, 0)

        camera.setLifecycleOwner(this)
        camera.addCameraListener(object: CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                val byte = result.data
                val bitmap = byteArrayToBitmap(byte)
                img_bukti_transfer.setImageBitmap(bitmap)

                path = saveImage(bitmap, this@KonfirmasiActivity)
            }
        })

        btn_ambil_foto.onClick {
            camera.takePicture()
            img_bukti_transfer.terlihat()
            btn_ambil_foto_lagi.terlihat()
            btn_ambil_foto.hilang()
        }

        btn_ambil_foto_lagi.onClick {
            path = ""
            img_bukti_transfer.hilang()
            btn_ambil_foto.terlihat()
            btn_ambil_foto_lagi.hilang()
        }

        btn_kirim.onClick {
            if (path == "") {
                toast("Silahkan ambil foto")
                return@onClick
            }

            Log.e("path", path)

            alert {
                isCancelable = false
                message = "Kirim foto bukti transfer?"
                positiveButton(R.string.ya) {
                    val file = File(path)
                    val image = RequestBody.create(MediaType.parse("image/*"), file)
                    val imageData = MultipartBody.Part.createFormData("file", file.name, image)

                    presenter.konfirmasiPembayaran(this@KonfirmasiActivity, orderId, imageData)
                }
                negativeButton(R.string.batal) {
                    it.dismiss()
                }
            }.show()
        }
    }

    override fun showLoading() {
        loading?.show()
    }

    override fun suksesKonfirmasi() {
        onBackPressed()
    }

    override fun error(msg: String) {
        alert {
            isCancelable = false
            message = msg
            okButton {
                it.dismiss()
            }
        }.show()
    }

    override fun hideLoading() {
        loading?.dismiss()
    }

    override fun onDetachView() {
        presenter.onDetach()
        presenter.dispo()
        loading = null
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
