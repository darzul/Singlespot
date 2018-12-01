package com.singlespot.android.demo.repository

import com.singlespot.android.demo.database.AppDatabase
import com.singlespot.android.demo.database.RPoint
import com.singlespot.android.demo.entity.Cluster
import com.singlespot.android.demo.entity.Point
import io.reactivex.Flowable

class PointLocalDataSource(private val database: AppDatabase) {

    private val pointDao
        get() = database.pointDao()

    fun count(): Int = pointDao.count()

    fun getAll(): Flowable<List<Cluster>> = pointDao.getAll()
        .map { points ->
            points.groupBy { it.clusterId }
                .map { Cluster(it.key, it.value.map(this::toEntity)) }
        }


    fun save(cluster: Cluster) = pointDao.insertAll(*cluster.points.map { toDatabase(cluster, it) }.toTypedArray())

    private fun toEntity(point: RPoint) =
        Point(point.id, point.latitude, point.longitude)

    private fun toDatabase(cluster: Cluster, point: Point) =
        RPoint(point.id, cluster.id, point.latitude, point.longitude)
}
