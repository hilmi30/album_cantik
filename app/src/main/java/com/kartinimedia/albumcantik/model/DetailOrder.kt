package com.kartinimedia.albumcantik.model

import com.google.gson.annotations.SerializedName

data class DetailOrder (
    @SerializedName("product")
    var product: DataProduct = DataProduct()
)