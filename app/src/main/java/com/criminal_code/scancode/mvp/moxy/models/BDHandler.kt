package com.criminal_code.scancode.mvp.moxy.models

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.criminal_code.scancode.utils.formatDate
import com.reactiveandroid.annotation.Column
import com.reactiveandroid.annotation.PrimaryKey
import com.reactiveandroid.annotation.Table
import java.util.*

val DATABASENAME = "goods.db"
val TABLENAME = "goods"
val ID: String = "id"
val NAME: String = "name"
val TEXT: String = "text"
val COST: String = "cost"
val BARCODE: String = "barcode"

class BDHandler(var context: Context): DataBaseHelper(context, DATABASENAME) {
    override fun onCreate(db: SQLiteDatabase?) {
        /*val createTable = "CREATE TABLE " + TABLENAME + " (" + NAME + " VARCHAR(256)," +
                BARCODE + " REAL," + TAXGROUP + " VARCHAR(256)," +
                NAME_1 + " VARCHAR(256)," + NAME_2 + " VARCHAR(256)," + TRIAL471 + " CHAR(1))"
        db?.execSQL(createTable)*/
    }

    companion object {
        private var db: BDHandler? = null
        fun init(context: Context) {
            if (db == null) {
                db = BDHandler(context)
            }
        }

        fun getDatabase(): BDHandler? = db
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun readUser(barcode: String): ArrayList<DB> {
        val dbList = ArrayList<DB>()
        val db = readableDatabase
        var cursor: Cursor? = null
        try {
            //cursor = db.rawQuery("select * from $TABLENAME ", null)
            cursor = db.rawQuery("select * from $TABLENAME WHERE $BARCODE='$barcode'", null)
        } catch (e: SQLiteException) {

            return ArrayList()
        }

        var id: Long
        var name: String
        var text: String
        var cost: Long
        var barcode: Long

        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                id = cursor.getLong(cursor.getColumnIndex(ID))
                name = cursor.getString(cursor.getColumnIndex(NAME))
                text = cursor.getString(cursor.getColumnIndex(TEXT))
                cost = cursor.getLong(cursor.getColumnIndex(COST))
                barcode = cursor.getLong(cursor.getColumnIndex(BARCODE))

                dbList.add(DB(id,name,text,cost,barcode))
                cursor.moveToNext()
            }
        }
        return dbList
    }

}