package com.example.simpletable

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.Exception

class DBHelper private constructor(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object{
            const val DATABASE_NAME = "Member.db"
            const val DATABASE_VERSION = 1
            const val TABLE_NAME = "user_table"
            const val ID_COL = "_id"
            const val FIRST_NAME_COL = "firstname"
            const val LAST_NAME_COL = "lastname"
            const val REWARD_COL = "reward"

            @Volatile
            private var instance: DBHelper? = null

            fun getInstance(context: Context) = instance ?: synchronized(DBHelper::class.java) {
                instance ?: DBHelper(context).also {
                    instance = it
                }
            }
        }

    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery = "CREATE TABLE $TABLE_NAME (" +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$FIRST_NAME_COL TEXT, " +
                "$LAST_NAME_COL TEXT, " +
                "$REWARD_COL INTEGER" +
                ")"

        db?.execSQL(createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }

    fun insertData(firstname: String, lastname: String, reward: String) {
        val db : SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(FIRST_NAME_COL, firstname)
            put(LAST_NAME_COL, lastname)
            put(REWARD_COL, reward)
        }

        db.insert(TABLE_NAME, null, contentValues)
    }

    fun updateData(id: String, firstname: String, lastname: String, reward: String) {
        val db : SQLiteDatabase = this.writableDatabase
        val myContentValues = ContentValues().apply {
            put(ID_COL, id)
            put(FIRST_NAME_COL, firstname)
            put(LAST_NAME_COL, lastname)
            put(REWARD_COL, reward)
        }

        db.update(TABLE_NAME, myContentValues, "$ID_COL = ?", arrayOf(id))
    }

    fun deleteData(id: String) {
        val db : SQLiteDatabase = this.writableDatabase
        db.delete(TABLE_NAME, "$ID_COL = ?", arrayOf(id))
    }

    fun getAllData(): String {
        var result = "No data in the database"

        val db : SQLiteDatabase = this.readableDatabase
        val cursor : Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        try {
            if(cursor.count != 0) {
                val stringBuffer = StringBuffer()
                while(cursor.moveToNext()) {
                    stringBuffer.append("ID :" + cursor.getInt(0).toString() + "\n")
                    stringBuffer.append("FIRST NAME :" + cursor.getString(1).toString() + "\n")
                    stringBuffer.append("LAST NAME :" + cursor.getString(2).toString() + "\n")
                    stringBuffer.append("REWARD :" + cursor.getInt(3).toString() + "\n\n")
                }
                result = stringBuffer.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if(cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }

        return result
    }
}