package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DataTentang (
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("title")
    var title: String = "",

    @SerializedName("description")
    var desc: String = ""
)