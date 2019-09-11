package com.kartinimedia.albumcantik.utils

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okio.BufferedSink
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class ProgressRequestBody(private val mFile: File, private val mListener: UploadCallbacks) : RequestBody() {

    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int)

        fun onError()

        fun onFinish()

        fun uploadStart()
    }

    init {
        mListener.uploadStart()
    }

    override fun contentType(): MediaType? {
        // i want to upload only images
        return MediaType.parse("image/*")
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(mFile)
        var uploaded: Long = 0

        fileInputStream.use { fileInput ->
            val read = fileInput.read(buffer)
            val handler = Handler(Looper.getMainLooper())
            while (read != -1) {
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                handler.post(ProgressUpdater(uploaded, fileLength))
            }
        }
    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) : Runnable {

        override fun run() {
            try {

                val progress = (100 * mUploaded / mTotal).toInt()

                if (progress == 100)
                    mListener.onFinish()
                else
                    mListener.onProgressUpdate(progress)
            } catch (e: ArithmeticException) {
                mListener.onError()
                e.printStackTrace()
            }

        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }
}