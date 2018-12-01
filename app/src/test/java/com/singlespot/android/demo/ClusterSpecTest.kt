package com.singlespot.android.demo

import com.singlespot.android.demo.business.ClusterSpec
import com.singlespot.android.demo.entity.Point
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ClusterSpecTest {

    private lateinit var clusterSpec: ClusterSpec

    @Before
    fun setUp() {
        clusterSpec = ClusterSpec()
        clusterSpec.distance = 1.5
    }

    @Test
    fun `clusterize return zero cluster if there's zero point`() {
        assertClusterCount(emptyList(), 0)
    }

    @Test
    fun `clusterize return one cluster if there's one point`() {
        assertClusterCount(listOf(POINT), 1)
    }

    @Test
    fun `clusterize should return 2 clusters when 2 close points and 1 far`() {
        assertClusterCount(listOf(POINT, CLOSE_POINT, FAR_POINT), 2)
    }

    @Test
    fun `clusters shouldn't contain same point`() {
        // POINT and NOT_SO_FAR_POINT shouldn't be in the same cluster
        assertClusterCount(listOf(POINT, NOT_SO_FAR_POINT), 2)

        // NOT_SO_FAR_POINT and CLOSE_POINT should be in the same cluster
        assertClusterCount(listOf(NOT_SO_FAR_POINT, CLOSE_POINT), 1)

        val clusters = clusterSpec.clusterize(listOf(POINT, CLOSE_POINT, NOT_SO_FAR_POINT, FAR_POINT))
        assertEquals(4, clusters.map { it.points }.flatten().size)
    }

    @Test
    fun `clusterize should cluster 2 close points`() {
        val cluster = clusterSpec.clusterize(POINT, listOf(POINT, CLOSE_POINT))
        assertEquals(2, cluster.pointCount)
    }

    @Test
    fun `clusterize shouldn't cluster 2 far points`() {
        val cluster = clusterSpec.clusterize(POINT, listOf(POINT, FAR_POINT))
        assertEquals(1, cluster.pointCount)
    }

    @Test
    fun `distance should be correct`() {
        val firstPoint = Point(0, 1.0, 1.0)
        val secondPoint = Point(0, 1.0, 2.0)
        val distance = clusterSpec.distance(firstPoint, secondPoint)
        assertEquals(1.0, distance, 0.0)
    }

    private fun assertClusterCount(points: List<Point>, expectedClusterCount: Int) {
        assertEquals(expectedClusterCount, clusterSpec.clusterize(points).size)
    }

    companion object {
        val POINT = Point(0L, 0.0, 0.0)
        val CLOSE_POINT = Point(1L, 1.0, 0.0)
        val NOT_SO_FAR_POINT = Point(2L, 2.0, 0.0)
        val FAR_POINT = Point(3L, 5.0, 0.0)
    }
}
