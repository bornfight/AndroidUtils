package com.bornfight.utils

import android.location.Location

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

object LocationUtil {

    private val EARTH_RADIUS_KM = 6371.0
    private val METERS_TO_KILOMETERS_SCALE = 1000.0

    fun latLngFromLocation(location: Location): LatLng {
        return LatLng(location.latitude, location.longitude)
    }


    fun getZoomLevel(meters: Float): Float {
        val scale = meters / 500
        return (16 - Math.log(scale.toDouble()) / Math.log(2.0)).toFloat()
    }

    fun getRadius(map: GoogleMap): Int {
        val zoom = map.cameraPosition.zoom

        val scale = Math.exp(16 - zoom * Math.log(2.0)).toFloat()
        return scale.toInt() * 2
    }


    fun calculateDistance(a: LatLng, b: LatLng): Float {
        val distance = FloatArray(1)
        Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, distance)
        return distance[0]
    }

}
