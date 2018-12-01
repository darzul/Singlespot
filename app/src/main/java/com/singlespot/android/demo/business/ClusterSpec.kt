package com.singlespot.android.demo.business

import com.singlespot.android.demo.entity.Cluster
import com.singlespot.android.demo.entity.Point

class ClusterSpec {

    var distance = 1.2

    fun clusterize(points: List<Point>): List<Cluster> {
        if (points.isEmpty()) return emptyList()

        val clusterizedPoints = HashMap<Point, Boolean>()
        points.forEach { clusterizedPoints[it] = false }

        val clusters = mutableListOf<Cluster>()
        var reference: Point? = points[0]
        while (reference != null) {
            val cluster = clusterize(reference, clusterizedPoints.entries.filter { !it.value }.map { it.key })
            clusters.add(cluster)
            cluster.points.forEach { clusterizedPoints[it] = true }

            reference = clusterizedPoints.keys.firstOrNull { clusterizedPoints[it] == false }
        }

        return clusters
    }

    internal fun clusterize(reference: Point, points: List<Point>): Cluster {
        return Cluster(reference.id, points.filter { distance(reference, it) < distance })
    }

    /**
     * for simplicity sake we use geometric distance
     */
    internal fun distance(startPoint: Point, endPoint: Point): Double {
        return Math.sqrt(Math.pow(startPoint.latitude - endPoint.latitude, 2.0)
                + Math.pow(startPoint.longitude - endPoint.longitude, 2.0)
        )
    }
}
