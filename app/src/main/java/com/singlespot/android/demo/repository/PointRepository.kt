package com.singlespot.android.demo.repository

import android.util.Log
import com.singlespot.android.demo.business.ClusterSpec
import com.singlespot.android.demo.entity.Cluster
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PointRepository(
    private val remoteDataSource: PointRemoteDataSource,
    private val localDataSource: PointLocalDataSource,
    private val clusterSpec: ClusterSpec
) {

    private var synchroniseQuery: Disposable? = null

    fun synchronise() {
        if (!needToSynchronize()) return

        synchroniseQuery?.dispose()
        synchroniseQuery = remoteDataSource.getPointIds()
            .flatMapObservable { Observable.fromIterable(it) }
            .flatMapSingle { remoteDataSource.getPoint(it).subscribeOn(Schedulers.computation()) }
            .toList()
            .map { clusterSpec.clusterize(it) }
            .flatMapObservable { Observable.fromIterable(it) }
            .flatMapCompletable { localDataSource.save(it) }
            .subscribe({}, { Log.e("Fetching error", "error", it) })
    }

    fun getAll(): Flowable<List<Cluster>> = localDataSource.getAll()

    private fun needToSynchronize() = localDataSource.count() == 0
}
