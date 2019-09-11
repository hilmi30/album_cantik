package com.kartinimedia.albumcantik.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.activity.UploadActivity
import com.kartinimedia.albumcantik.adapter.PhotoAdapter
import com.kartinimedia.albumcantik.utils.*
import kotlinx.android.synthetic.main.fragment_photo.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.uiThread


class PhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val position = arguments?.getInt(Const.position)
        val titleFolder = arguments?.getString(Const.title)

        pb_photo.terlihat()

        doAsync {
            val photoAdapter = PhotoAdapter(fnImagesPath(ctx), position as Int) {
                (activity as UploadActivity).getImage(it)
            }

            uiThread {
                pb_photo.hilang()
                rv_photo.apply {
                    adapter = photoAdapter
                    layoutManager = GridLayoutManager(ctx, 4)
                    addItemDecoration(ItemDecoratorGrid(8))
                    setHasFixedSize(true)
                }
            }
        }

        tv_folder_title.text = titleFolder

        tv_folder_title.onClick {
            activity?.supportFragmentManager?.inTransaction {
                remove(this@PhotoFragment)
            }
        }
    }
}
