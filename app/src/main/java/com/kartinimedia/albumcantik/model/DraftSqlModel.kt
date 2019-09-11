package com.kartinimedia.albumcantik.model

import com.kartinimedia.albumcantik.utils.Const
import hinl.kotlin.database.helper.Database
import hinl.kotlin.database.helper.Schema

@Database(Const.draftTable)
data class DraftSqlModel(
    @Schema(generatedId = true, field = "Id", autoIncrease = true, nonNullable = true) val id: Int? = 0,
    @Schema("imageKey") var imageKey: Int?,
    @Schema("image") var image: String?,
    @Schema("productId") var productId: Int?
)