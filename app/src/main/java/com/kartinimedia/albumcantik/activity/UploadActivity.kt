package com.kartinimedia.albumcantik.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaopiz.kprogresshud.KProgressHUD
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.adapter.GalleryAdapter
import com.kartinimedia.albumcantik.adapter.PageAdapter
import com.kartinimedia.albumcantik.adapter.PreviewAdapter
import com.kartinimedia.albumcantik.model.*
import com.kartinimedia.albumcantik.presenter.UploadPresenter
import com.kartinimedia.albumcantik.fragment.PhotoFragment
import com.kartinimedia.albumcantik.utils.*
import com.kartinimedia.albumcantik.view.UploadView
import kotlinx.android.synthetic.main.activity_upload.*
import me.echodev.resizer.Resizer
import okhttp3.MediaType
import okhttp3.MultipartBody
import org.jetbrains.anko.sdk27.coroutines.onClick
import ss.com.bannerslider.Slider
import java.io.File
import okhttp3.RequestBody
import org.jetbrains.anko.*


class UploadActivity : AppCompatActivity(), UploadView {

    private val presenter = UploadPresenter()

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val imgPreview: HashMap<Int, String> = hashMapOf()
    private val imgChanged: HashMap<Int, String> = hashMapOf()
    private val imgGallery: MutableList<GalleryModel> = mutableListOf()
    private var imageList: ArrayList<ImageData> = arrayListOf()

    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var previewAdapter: PreviewAdapter
    private lateinit var fragManager: FragmentManager
    private lateinit var photoFrag: Fragment

    private var isPhotoOpen = false
    private lateinit var loadingSlider: KProgressHUD
    private var productId = 0
    private var photoUploaded = 0
    private var totalFoto = 0
    private var isDraft = false
    private var harga = 0
    private var hargaReseller = 0
    private var namaProduct = ""
    private var image = ""
    private lateinit var uploadProgress: TextView
    private lateinit var alertUpload: DialogInterface
    private lateinit var database: SQLHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        onAttachView()
    }

    override fun showLoadingSlider() {
        loadingSlider.show()
    }

    override fun hideLoadingSlider() {
        loadingSlider.dismiss()
    }

    override fun error(msg: String, id: String) {
        if (id == Const.upload) alertUpload.dismiss()
        presenter.dispose()
        alert {
            isCancelable = false
            message = msg
            positiveButton(getString(R.string.coba_lagi)) {
                when (id) {
                    Const.slider -> {
                        presenter.getSliderProduct(this@UploadActivity, productId)
                        if (isDraft) presenter.getDraftByProductId(this@UploadActivity, productId)
                    }
                    Const.upload -> {
                        photoUploaded = 0
                        alertLoadingUpload()
                        uploadImage(0, imageList.size)
                    }
                    Const.draft -> {
                        presenter.getDraftByProductId(this@UploadActivity, productId)
                    }
                }
            }
            negativeButton(getString(R.string.keluar)) {
                it.dismiss()
            }
        }.show()
    }

    @SuppressLint("SetTextI18n")
    override fun showData(it: DataProduct) {
        val data = it.slider
        tv_page.text = "Hal 1 dari ${data.size}"

        Slider.init(PicassoImageLoadingService())
        banner_slider1.apply {
            setAdapter(PageAdapter(data))
            setSlideChangeListener {
                tv_page.text = "Hal ${it.plus(1)} dari ${data.size}"
            }
            hideIndicators()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onAttachView() {
        presenter.onAttach(this)

        database = SQLHelper(this)
        database.createTable(DraftSqlModel::class)

        fragManager = supportFragmentManager
        photoFrag = PhotoFragment()

        totalFoto = intent.getIntExtra(Const.totalFoto, 0)
        productId = intent.getIntExtra(Const.id, 0)
        isDraft = intent.getBooleanExtra(Const.isDraft, false)
        harga = intent.getIntExtra(Const.harga, 0)
        hargaReseller = intent.getIntExtra(Const.hargaReseller, 0)
        namaProduct = intent.getStringExtra(Const.namaProduct) as String
        image = intent.getStringExtra(Const.image) as String

        loadingSlider = kProgressHUD(this, getString(R.string.mohon_tunggu))

        presenter.getSliderProduct(this, productId)

        previewAdapter = PreviewAdapter(totalFoto, imgPreview) {
            // fungsi hapus foto preview
            imgPreview.remove(it)
            previewAdapter.notifyItemChanged(it)

            val product = database.get(DraftSqlModel::class) {
                eq("productId", productId)
            }

            product?.forEach { data ->
                if (data.imageKey == it) {
                    database.delete(data)
                }
            }
        }

        rv_preview.apply {
            adapter = previewAdapter
            layoutManager = LinearLayoutManager(this@UploadActivity, LinearLayout.HORIZONTAL, false)
            addItemDecoration(ItemDecoratorHorizontal(16))
            setHasFixedSize(true)
        }

        btn_upload.onClick {
            if (!checkJumlahFoto()) return@onClick

            if (isDraft) {
                if (imgChanged.isEmpty())
                    goToOrderActivity()
                else
                    alert {
                        message = getString(R.string.terdeteksi_perubahan)
                        isCancelable = false
                        positiveButton(R.string.ya) {
                            upload(imgChanged)
                        }
                        negativeButton(R.string.batal) {
                            it.dismiss()
                        }
                    }.show()
            } else {
                upload(imgPreview)
            }
        }

        galleryAdapter = GalleryAdapter(imgGallery) { pos: Int, galleryModel: GalleryModel ->
                val bundle = Bundle()
                bundle.putInt(Const.position, pos)
                bundle.putString(Const.title, galleryModel.strFolder)
                photoFrag.arguments = bundle

                fragManager.inTransaction {
                    replace(R.id.photo_container, photoFrag)
                }

                isPhotoOpen = true
            }

        rv_gallery.apply {
            adapter = galleryAdapter
            layoutManager = LinearLayoutManager(this@UploadActivity)
            addItemDecoration(ItemDecorator(16))
        }

        checkPermission()

        swipe_gallery.setOnRefreshListener {
            checkPermission()
            swipe_gallery.isRefreshing = false
        }

        btn_batal.onClick {
            finish()
        }

        if (isDraft) {
            checkImgChanged()
            btn_upload.text = getString(R.string.order)
            presenter.getDraftByProductId(this, productId)
        } else {
            btn_upload_ulang.hilang()
            val draft = database.get(DraftSqlModel::class) {
                eq("productId", productId)
            }

            draft?.forEach {
                imgPreview[(it.imageKey as Int).minus(1)] = it.image as String
                previewAdapter.notifyDataSetChanged()
            }
        }

        btn_upload_ulang.onClick {
            if (checkJumlahFoto()) upload(imgChanged)
        }
    }

    private fun checkJumlahFoto(): Boolean {
        return if (imgPreview.size != totalFoto) {
            alert {
                message = getString(R.string.untuk_bisa_upload)
                okButton {
                    it.dismiss()
                }
            }.show()
            false
        } else {
            true
        }
    }

    private fun checkImgChanged() {
        if (imgChanged.size == 0) btn_upload_ulang.hilang()
        else btn_upload_ulang.terlihat()
    }

    private fun goToOrderActivity() {
        startActivity<OrderActivity>(
            Const.productId to productId,
            Const.harga to harga,
            Const.hargaReseller to hargaReseller,
            Const.namaProduct to namaProduct,
            Const.image to image,
            Const.isDetailOrder to false
        )
    }

    @SuppressLint("SetTextI18n")
    private fun upload(data: HashMap<Int, String>) {

        var valid = true
        data.forEach {
            val file = File(it.value)
            val fileSize = file.length() / 1024

            if (fileSize < 1024) valid = false
        }

        if (!valid) {
            alertSize()
            return
        }

        alert {
            message = getString(R.string.yakin_upload)
            isCancelable = false
            positiveButton(R.string.ya) {
                if (imgPreview.isNotEmpty()) {
                    photoUploaded = 0
                    alertLoadingUpload()

                    imageList.clear()
                    data.toSortedMap()
                    data.forEach {
                        imageList.add(ImageData(it.key, it.value))
                    }

                    uploadProgress.text = "$photoUploaded/${imageList.size}"
                    uploadImage(0, imageList.size)
                }
            }
            negativeButton(R.string.batal) {
                it.dismiss()
            }
        }.show()
    }

    override fun showDraft(files: List<DataDraftByProductId>?) {
        imgPreview.clear()
        files?.forEach {
            imgPreview[(it.number.toInt()).minus(1)] = it.image
            previewAdapter.notifyDataSetChanged()
        }
    }

    private fun setUpload(index: Int) {
        val increment = index.plus(1)
        uploadImage(increment, imageList.size)
    }

    override fun onDetachView() {
        presenter.onDetach()
        presenter.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun prosesUpload(index: Int) {
        setProgressUpload()
        setUpload(index)
    }

    @SuppressLint("SetTextI18n")
    private fun setProgressUpload() {
        photoUploaded = photoUploaded.plus(1)
        uploadProgress.text = "$photoUploaded/${imageList.size}"
    }

    override fun suksesUploadSemua() {
        setProgressUpload()

        Handler().postDelayed({
            alertUpload.dismiss()
            alert {
                isCancelable = false
                message = getString(R.string.upload_berhasil)
                positiveButton("Isi form order") {
                    if (!checkJumlahFoto()) {
                        return@positiveButton
                    }
                    goToOrderActivity()
                }
                negativeButton("Nanti saja") {
                    it.dismiss()
                }
            }.show()
        }, 3000)

    }

    private fun uploadImage(index: Int, size: Int) {

        val key = imageList[index].key
        val value = imageList[index].value

        val file = File(value)
        val image = RequestBody.create(MediaType.parse("image/*"), file)
        val imageData = MultipartBody.Part.createFormData("file", file.name, image)

        presenter.upload(this@UploadActivity, imageData, productId,
            getToken(this@UploadActivity), key.plus(1), index, size)
    }

    @SuppressLint("SetTextI18n")
    private fun alertLoadingUpload() {
        alertUpload = alert {
            isCancelable = false
            customView {
                verticalLayout {
                    padding = dip(32)
                    textView {
                        text = getString(R.string.sedang_proses_upload)
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }.lparams {
                        margin = dip(16)
                        width = matchParent
                        height = wrapContent
                    }

                    horizontalProgressBar {
                        isIndeterminate = true
                    }

                    uploadProgress = textView {
                        text = "0/${imageList.size}"
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }.lparams {
                        top = dip(16)
                        width = matchParent
                        height = wrapContent
                    }
                }
            }
            negativeButton(getString(R.string.batal)) {
                presenter.dispose()
                it.dismiss()
            }
        }.show()
    }

    private fun checkPermission() {
        Permissions.check(this@UploadActivity, permissions, null, null, object: PermissionHandler() {
            override fun onGranted() {
                setGallery()
            }

            override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                alert {
                    message = getString(R.string.izin_akses_gambar)
                    positiveButton(getString(R.string.izinkan)) {
                        checkPermission()
                    }
                    negativeButton(getString(R.string.tutup)) {
                        it.dismiss()
                    }

                }.show()
            }
        })
    }

    private fun setGallery() {
        pb_gallery.terlihat()
        doAsync {
            imgGallery.apply {
                clear()
                addAll(fnImagesPath(this@UploadActivity))
            }

            uiThread {
                pb_gallery.hilang()
                galleryAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onBackPressed() {
        if (isPhotoOpen) {
            fragManager.inTransaction {
                remove(photoFrag)
            }
            isPhotoOpen = false
        } else {
            finish()
        }
    }

    private fun alertSize() {
        alert {
            title = getString(R.string.gambar_tidak_sesuai)
            message = getString(R.string.gambar_harus_1mb)
            okButton { dialog ->
                dialog.dismiss()
            }
        }.show()
    }

    fun getImage(image: String) {

        val file = File(image)
        val size = file.length() / 1024

        if (size.toDouble() < 1024) {
            alertSize()
            return
        }

        if (size.toDouble() > 2048) {
            val resizedImage = Resizer(this)
                .setTargetLength(2000)
                .setQuality(100)
                .setSourceImage(file)
                .resizedFile

            imgChanged[getIndex()] = resizedImage.path
            imgPreview[getIndex()] = resizedImage.path
        } else {
            imgChanged[getIndex()] = image
            imgPreview[getIndex()] = image
        }

        previewAdapter.indexPlusOne()
        rv_preview.scrollToPosition(getIndex().plus(1))
        previewAdapter.notifyDataSetChanged()

        val count = database.count(DraftSqlModel::class) {
            eq("productId", productId)
        }

        val product = database.get(DraftSqlModel::class) {
            eq("productId", productId)
        }

        if (count >= 1) {
            product?.forEach {
                if (it.imageKey == getIndex()) {
                    database.delete(it)
                }
            }

            val data = DraftSqlModel(imageKey = getIndex(), productId = productId, image = image)
            database.insert(data)
        } else {
            val data = DraftSqlModel(imageKey = getIndex(), productId = productId, image = image)
            database.insert(data)
        }

        if (isDraft) checkImgChanged()
    }

    private fun getIndex(): Int {
        return previewAdapter.getIndex()
    }
}
