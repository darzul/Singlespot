package com.singlespot.android.demo.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.singlespot.android.demo.database.AppDatabase.Companion.TABLE_POINT
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface PointDao {

    @Query("SELECT COUNT(*) FROM $TABLE_POINT")
    fun count(): Int

    @Query("SELECT * FROM $TABLE_POINT")
    fun getAll(): Flowable<List<RPoint>>

    @Insert
    fun insertAll(vararg points: RPoint): Completable
}

@Entity(tableName = TABLE_POINT)
data class RPoint(
    @PrimaryKey var id: Long,
    @ColumnInfo(name = "cluster_id") var clusterId: Long,
    @ColumnInfo(name = "latitude") var latitude: Double,
    @ColumnInfo(name = "longitude") var longitude: Double
)
