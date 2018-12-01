package com.singlespot.android.demo

import android.app.Application
import androidx.room.Room
import com.singlespot.android.demo.business.ClusterSpec
import com.singlespot.android.demo.database.AppDatabase
import com.singlespot.android.demo.repository.PointLocalDataSource
import com.singlespot.android.demo.repository.PointRemoteDataSource
import com.singlespot.android.demo.repository.PointRepository
import com.singlespot.android.demo.repository.PointService
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MyApplication : Application() {

    lateinit var pointRepository: PointRepository

    override fun onCreate() {
        super.onCreate()

        val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name")
            .fallbackToDestructiveMigration()
            .build()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://static.singlespot.com/tests/mobile5/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val pointService = retrofit.create(PointService::class.java)
        val pointRemoteDataSource = PointRemoteDataSource(pointService)
        val pointLocalDataSource = PointLocalDataSource(database)
        val clusterSpec = ClusterSpec()
        pointRepository =
                PointRepository(pointRemoteDataSource, pointLocalDataSource, clusterSpec)

        Schedulers.computation().scheduleDirect {
            pointRepository.synchronise()
        }
    }
}
