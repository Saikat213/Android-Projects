package com.example.fundooapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fundooapp.model.NotesData

class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val db : SQLiteDatabase = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery = "create table $TABLE_NAME($ID INTEGER primary key autoincrement, $TITLE TEXT, $CONTENT TEXT, $ARCHIVED TEXT)"
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropQuery = "drop table if exists $TABLE_NAME"
        db?.execSQL(dropQuery)
        onCreate(db)
    }

    fun saveData(noteData : NotesData) {
        val content = ContentValues()
        content.put(FIRESTOREID, noteData.ID)
        content.put(TITLE, noteData.Title)
        content.put(CONTENT, noteData.Content)
        content.put(ARCHIVED, noteData.Archive)
        db.insert(TABLE_NAME, null, content)
        db.close()
    }

    fun deleteData(id : String) {
        db.delete(TABLE_NAME, "ID = ?", arrayOf(id))
    }

    fun updateData(title: String, noteContent: String, id: String) {
        val content = ContentValues()
        content.put(FIRESTOREID, id)
        content.put(TITLE, title)
        content.put(CONTENT, noteContent)
        db.update(TABLE_NAME, content, "ID = ?", arrayOf(id))
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