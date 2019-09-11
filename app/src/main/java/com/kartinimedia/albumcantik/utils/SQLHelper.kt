package com.kartinimedia.albumcantik.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import hinl.kotlin.database.helper.SQLiteDatabaseHelper

class SQLHelper(context: Context): SQLiteDatabaseHelper(context, Const.dbName, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        //
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //
    }
}