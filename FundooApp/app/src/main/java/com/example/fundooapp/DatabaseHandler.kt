package com.example.fundooapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val database : SQLiteDatabase = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery = "create table $TABLE_NAME($ID INTEGER primary key autoincrement, $TITLE TEXT, $CONTENT TEXT, $ARCHIVED TEXT)"
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropQuery = "drop table if exists $TABLE_NAME"
        db?.execSQL(dropQuery)
        onCreate(db)
    }

    fun saveData(title: String, noteContent : String, archive : String) {
        val content = ContentValues()
        content.put(TITLE, title)
        content.put(CONTENT, noteContent)
        content.put(ARCHIVED, archive)
        database.insert(TABLE_NAME, null, content)
        database.close()
    }

    fun deleteData(title : String) {
        database.delete(TABLE_NAME, "TITLE = ?", arrayOf(title))
    }

    fun updateData(title: String, noteContent: String, searchData : String) {
        val content = ContentValues()
        content.put(TITLE, title)
        content.put(CONTENT, noteContent)
        database.update(TABLE_NAME, content, "TITLE = ?", arrayOf(searchData))
    }

    fun updateArchive(title: String) {
        val content = ContentValues()
        content.put(ARCHIVED, "true")
        database.update(TABLE_NAME, content, "TITLE = ?", arrayOf(title))
    }

    fun retriveData(archive : String) : Cursor {
        val readDataDB = this.readableDatabase
        val getDataQuery = "SELECT * FROM $TABLE_NAME WHERE ARCHIVED = '$archive'"
        return readDataDB.rawQuery(getDataQuery, null)
    }

    companion object {
        const val DATABASE_NAME = "Notes"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "NotesTable"
        const val ID = "id"
        const val TITLE = "Title"
        const val CONTENT = "Content"
        const val DATETIME = "DateTime"
        const val ARCHIVED = "Archived"
        const val FIRESTOREID = "FirestoreID"
    }
}