package com.singlespot.android.demo.repository

import com.singlespot.android.demo.entity.Point
import io.reactivex.Single

class PointRemoteDataSource(private val pointService: PointService) {

    fun getPointIds(): Single<List<Long>> {
        return pointService.fetchPointUrls()
            .map { urls -> urls.mapNotNull { getPointId(it) } }
    }

    fun getPoint(id: Long): Single<Point> {
        return pointService.fetchPoint(id).map { Point(id, it.latitude, it.longitude) }
    }

    internal fun getPointId(pointUrl: String): Long? {
        val pointId = pointUrl.split("/").last()
        if (pointId.isBlank()) return null
        return try {
            pointId.toLong()
        } catch (e: NumberFormatException) {
            null
        }
    }
}
