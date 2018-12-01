package com.singlespot.android.demo

import com.singlespot.android.demo.repository.PointRemoteDataSource
import com.singlespot.android.demo.repository.PointService
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PointRemoteDataSourceTest {

    @Mock lateinit var pointService: PointService
    @InjectMocks lateinit var pointRemoteDataSource: PointRemoteDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    fun `get point id from a real point url`() {
        assertEquals(POINT_ID, pointRemoteDataSource.getPointId(REAL_POINT_URL))
    }

    @Test
    fun `get point id should return null when url is empty`() {
        assertNull(pointRemoteDataSource.getPointId(""))
    }

    @Test
    fun `get point id should return null when point id isn't a number`() {
        assertNull(pointRemoteDataSource.getPointId(FAKE_POINT_URL))
    }

    companion object {
        const val REAL_POINT_URL = "http://static.singlespot.com/tests/mobile5/points/900"
        const val FAKE_POINT_URL = "http://static.singlespot.com/tests/mobile5/points/azerty"
        const val POINT_ID = 900L
    }
}