package com.singlespot.android.demo.repository

import com.google.gson.annotations.SerializedName

data class PointJson(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)
