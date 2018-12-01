package com.singlespot.android.demo.repository

import com.singlespot.android.demo.repository.PointJson
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PointService {
    @GET("points.json")
    fun fetchPointUrls(): Single<List<String>>

    @GET("points/{id}")
    fun fetchPoint(@Path("id") id: Long): Single<PointJson>
}
