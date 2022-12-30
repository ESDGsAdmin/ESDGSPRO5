package com.example.esdgspro

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DBNAME, null, version) {
    companion object {
        private const val DBNAME = "Esdgs.db"
        private const val version = 1
    }

    override fun onCreate(database: SQLiteDatabase?) {
        val sb = StringBuilder()
        sb.append("CREATE TABLE esdgs (ingredient_id varchar(14) primary key, ingredient_name varchar(100), product_class int, purchase_date date, expiry_date date, quantity int, state int, image blob, reg_date date, reg_user varchar(20), upd_date date, upd_user varchar(20));")
        val sql = sb.toString()
        database?.execSQL(sql)
        println(sql)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    override fun onOpen(database: SQLiteDatabase?) {
        //super.onOpen(database)
        println("sa")
        //val test = database?.execSQL("update esdgs set ingredient_name = 'ブリ', product_class = '魚', purchase_date = '2022-12-21', expiry_date = '2022-12-23', quantity = 1, state = 0, image = null, registed = '2022-12-21', register = 'shimizu', modified = '2022-12-21', modifier = 'shimizu' where ingredient_id = 2")
        //println(test)
    }
}
