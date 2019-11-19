package com.kartinimedia.albumcantik.utils

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.provider.MediaStore
import android.app.Activity
import android.content.Context
import android.database.Cursor
import com.kaopiz.kprogresshud.KProgressHUD
import com.kartinimedia.albumcantik.model.GalleryModel
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.BitmapFactory
import com.kartinimedia.albumcantik.R
import retrofit2.HttpException
import java.io.*
import java.net.ConnectException


fun View.terlihat() {
    visibility = View.VISIBLE
}

fun View.hilang() {
    visibility = View.GONE
}

fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun getToken(context: Context): String {
    val userPref = context.getSharedPreferences(Const.userPref, Context.MODE_PRIVATE)
    return userPref.getString(Const.token, "") as String
}

fun getRoles(context: Context): String {
    val userPref = context.getSharedPreferences(Const.userPref, Context.MODE_PRIVATE)
    return userPref.getString(Const.roles, "customer") as String
}

fun getStatus(context: Context): Boolean {
    val userPref = context.getSharedPreferences(Const.userPref, Context.MODE_PRIVATE)
    return userPref.getBoolean(Const.status, false)
}

fun getGenderId1(context: Context): Int {
    val userPref = context.getSharedPreferences(Const.genderData, Context.MODE_PRIVATE)
    return userPref.getInt(Const.genderId1, 2)
}

fun getGenderId2(context: Context): Int {
    val userPref = context.getSharedPreferences(Const.genderData, Context.MODE_PRIVATE)
    return userPref.getInt(Const.genderId2, 3)
}

fun getGenderNama1(context: Context): String {
    val userPref = context.getSharedPreferences(Const.genderData, Context.MODE_PRIVATE)
    return userPref.getString(Const.genderName1, "Laki-laki") as String
}

fun getGenderNama2(context: Context): String {
    val userPref = context.getSharedPreferences(Const.genderData, Context.MODE_PRIVATE)
    return userPref.getString(Const.genderName2, "Perempuan") as String
}

fun getAlamatUtama(context: Context): Int {
    val userPref = context.getSharedPreferences(Const.userPref, Context.MODE_PRIVATE)
    return userPref.getInt(Const.alamat, 0)
}

fun getEmail(context: Context): String {
    val userPref = context.getSharedPreferences(Const.userPref, Context.MODE_PRIVATE)
    return userPref.getString(Const.email, "") as String
}

fun fetchGalleryImages(context: Activity): ArrayList<String> {
    val galleryImageUrls: ArrayList<String> = ArrayList()
    val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID) //get all columns of type images
    val orderBy = MediaStore.Images.Media.DATE_TAKEN //order data by date

    val imagecursor = context.managedQuery(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
        null, null, "$orderBy DESC"
    ) //get all data in Cursor by sorting in DESC order

    for (i in 0 until imagecursor.count) {
        imagecursor.moveToPosition(i)
        val dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA) //get column index
        galleryImageUrls.add(imagecursor.getString(dataColumnIndex)) //get Image from column index

    }
    // Log.e("fatch in", "images")
    return galleryImageUrls
}

@SuppressLint("Recycle")
fun fnImagesPath(context: Context): MutableList<GalleryModel> {

    val imgGallery: MutableList<GalleryModel> = mutableListOf()

    var intPosition = 0
    var booleanFolder = false
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val cursor: Cursor
    val columnIndexData: Int
    val columnIndexFolderName: Int

    var absolutePathOfImage: String?

    val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

    val orderBy = MediaStore.Images.Media.DATE_TAKEN
    cursor = context.contentResolver.query(uri, projection, null, null, "$orderBy DESC") as Cursor

    columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
    columnIndexFolderName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
    while (cursor.moveToNext()) {
        absolutePathOfImage = cursor.getString(columnIndexData)
        // Log.e("Column", absolutePathOfImage)
        // Log.e("Folder", cursor.getString(columnIndexFolderName))

        for (i in imgGallery.indices) {
            if (imgGallery[i].strFolder == cursor.getString(columnIndexFolderName)) {
                booleanFolder = true
                intPosition = i
                break
            } else {
                booleanFolder = false
            }
        }

        if (booleanFolder) {
            val allPath = ArrayList<String>()
            allPath.addAll(imgGallery[intPosition].allImagePath)
            allPath.add(absolutePathOfImage)
            imgGallery[intPosition].allImagePath = allPath

        } else {
            val allPath = ArrayList<String>()
            allPath.add(absolutePathOfImage)
            val objModel = GalleryModel()
            objModel.strFolder = cursor.getString(columnIndexFolderName)
            objModel.allImagePath = allPath
            imgGallery.add(objModel)
        }
    }

    return imgGallery
}

fun kProgressHUD(context: Context, msg: String): KProgressHUD {
    return KProgressHUD.create(context)
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setLabel(msg)
        .setCancellable(false)
        .setAnimationSpeed(2)
        .setDimAmount(0.5f)
}

// simpan gambar ke storage
fun saveImage(myBitmap: Bitmap?, c: Context): String {
    val imageDirectory = "/BuktiTransferPhotoBook"
    val bytes = ByteArrayOutputStream()
    myBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val wallpaperDirectory = File(
        (Environment.getExternalStorageDirectory()).toString() + imageDirectory
    )
    // have the object build the directory structure, if needed.
    Log.d("fee",wallpaperDirectory.toString())
    if (!wallpaperDirectory.exists()) wallpaperDirectory.mkdirs()

    try
    {
        Log.d("heel",wallpaperDirectory.toString())
        val f = File(wallpaperDirectory, ((Calendar.getInstance()
            .timeInMillis).toString() + ".jpg"))
        f.createNewFile()
        val fo = FileOutputStream(f)
        fo.write(bytes.toByteArray())
        MediaScannerConnection.scanFile(c,
            arrayOf(f.path),
            arrayOf("image/jpeg"), null)
        fo.close()
        Log.d("TAG", "File Saved::--->" + f.absolutePath)

        return f.absolutePath
    }
    catch (e1: IOException) {
        e1.printStackTrace()
    }

    return ""
}

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
    val arrayInputStream = ByteArrayInputStream(byteArray)
    return BitmapFactory.decodeStream(arrayInputStream)
}

fun checkRole(context: Context): Int {
    var role = 0
    val userPref = context.getSharedPreferences(Const.userPref, Context.MODE_PRIVATE)

    when (userPref.getString(Const.roles, "customer")) {
        Const.customer -> role = 0
        Const.reseller -> role = 1
    }
    return role
}

fun checkError(it: Throwable): Boolean {
    return it is ConnectException || it is HttpException
}
