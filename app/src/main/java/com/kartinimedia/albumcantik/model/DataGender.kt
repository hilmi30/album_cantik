package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DataGender (
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("gender")
    var gender: String = ""
)