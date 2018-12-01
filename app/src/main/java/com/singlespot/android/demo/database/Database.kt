package com.singlespot.android.demo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.singlespot.android.demo.database.PointDao
import com.singlespot.android.demo.database.RPoint

@Database(entities = [RPoint::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val TABLE_POINT = "points"
    }

    abstract fun pointDao(): PointDao
}
