package com.example.fundooapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        const val DATABASE_NAME = "Notes"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "NotesTable"
        const val ID = ""
        const val TITLE = ""
        const val CONTENT = ""
        const val DATETIME = ""
    }
}