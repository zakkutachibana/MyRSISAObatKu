package com.zak.rsisaobatku.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Medicine::class], version = 1)
abstract class MedicineDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    companion object {
        @Volatile
        private var INSTANCE: MedicineDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): MedicineDatabase {
            if (INSTANCE == null) {
                synchronized(MedicineDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        MedicineDatabase::class.java, "medicine_database")
                        .build()
                }
            }
            return INSTANCE as MedicineDatabase
        }
    }
}