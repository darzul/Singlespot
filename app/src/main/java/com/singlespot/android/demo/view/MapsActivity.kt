package com.singlespot.android.demo.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.singlespot.android.demo.MyApplication
import com.singlespot.android.demo.R
import com.singlespot.android.demo.entity.Cluster
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private var clusters: List<Cluster>? = null
    private var clusterPointsQuery: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        val pointRepository = (application as MyApplication).pointRepository
        clusterPointsQuery = pointRepository.getAll()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                clusters = it
                displayPoints()
            }, { Log.e("ERROR", "fetch points from DB error", it) })
    }

    private fun displayPoints() {
        val googleMap = this.googleMap ?: return
        val clusters = this.clusters ?: return

        clusters
            .map { MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("${it.id}") }
            .forEach { googleMap.addMarker(it) }

        val maxLat = clusters.map { it.latitude }.max() ?: 0.0
        val maxLng = clusters.map { it.longitude }.max() ?: 0.0
        val maxLatLng = LatLng(maxLat, maxLng)

        val minLat = clusters.map { it.latitude }.min() ?: 0.0
        val minLng = clusters.map { it.longitude }.min() ?: 0.0
        val minLatLng = LatLng(minLat, minLng)

        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds(minLatLng, maxLatLng), 0))
    }

    override fun onDestroy() {
        clusterPointsQuery?.dispose()
        googleMap = null
        super.onDestroy()
    }
}
