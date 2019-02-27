package com.bornfight.utils

import android.location.Location

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

/**
 * A helper object containing various methods for work with maps.
 */
object LocationUtil {

    private val EARTH_RADIUS_KM = 6371.0
    private val METERS_TO_KILOMETERS_SCALE = 1000.0

    /**
     * Calculates the zoom level for use with [CameraUpdateFactory] the following equation:
     *   16 - [Math.log] (meters/500) / [Math.log] (2)
     *
     * @param meters the given radius in meters
     * @return the equation result
     */
    fun getZoomLevel(meters: Float): Float {
        val scale = meters / 500
        return (16 - Math.log(scale.toDouble()) / Math.log(2.0)).toFloat()
    }

    /**
     * Calculates the radius with the following equation: (16 - zoom * [Math.log] (2)) * 2
     * @param map [GoogleMap] instance, from which the zoom is fetched.]
     * @return the equation result
     */
    fun getRadius(map: GoogleMap): Int {
        val zoom = map.cameraPosition.zoom

        val scale = Math.exp(16 - zoom * Math.log(2.0)).toFloat()
        return scale.toInt() * 2
    }

}

/**
 * Calculates the distance between the two [LatLng] objects using the [Location.distanceBetween] method.
 */
fun LatLng.distanceTo(a: LatLng): Float {
    val distance = FloatArray(1)
    Location.distanceBetween(a.latitude, a.longitude, latitude, longitude, distance)
    return distance[0]
}

/**
 * Performs a [Location] - [LatLng] cast
 */
fun Location.latLng(): LatLng = LatLng(latitude, longitude)

