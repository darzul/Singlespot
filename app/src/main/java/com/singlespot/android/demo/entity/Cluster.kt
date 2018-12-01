package com.singlespot.android.demo.entity

class Cluster(val id: Long, val points: List<Point>) {

    val pointCount = points.size

    val latitude: Double
        get() = points[0].latitude

    val longitude: Double
        get() = points[0].longitude
}
