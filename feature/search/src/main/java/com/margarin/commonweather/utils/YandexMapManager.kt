package com.margarin.commonweather.utils

import android.content.Context
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map

class YandexMapManager (private val context: Context){


    fun mapMoveToPosition(map: Map, lat: String, lon: String) {
        map.move(
            CameraPosition(
                Point(lat.toDouble(), lon.toDouble()),
                6.0f,
                0.0f,
                0.0f
            ),
            LINEAR_ANIMATION
        ) {}
    }

    fun changeZoomByStep(value: Float, map: Map) {
        with(map.cameraPosition) {
            map.move(
                CameraPosition(target, zoom + value, azimuth, tilt),
                SMOOTH_ANIMATION,
                null,
            )
        }
    }

    fun configureMap(map: Map) {
        MapKitFactory.initialize(context)
        map.move(START_POSITION, LINEAR_ANIMATION) {}
    }

    companion object {
        private val LINEAR_ANIMATION = Animation(Animation.Type.LINEAR, 1f)
        private val SMOOTH_ANIMATION = Animation(Animation.Type.SMOOTH, 0.4f)
        private val START_POSITION = CameraPosition(
            Point(55.0, 50.0),
            4.0f,
            0.0f,
            0.0f
        )
    }
}