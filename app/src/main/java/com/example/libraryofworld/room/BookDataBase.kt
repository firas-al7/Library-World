package com.example.libraryofworld.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database([FavoriteBookEntity::class,DownloadBookEntity::class], version = 6)
abstract class BookDataBase:RoomDatabase() {

    abstract fun bookDao() : BookDao

    companion object{
        private var instance : BookDataBase? = null

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE FavoriteBookEntity ADD COLUMN imageUrl TEXT")
            }
        }
        fun getInstance(context : Context):BookDataBase{

            if (instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDataBase::class.java,
                    "book databse")
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return instance!!
        }
    }
}
