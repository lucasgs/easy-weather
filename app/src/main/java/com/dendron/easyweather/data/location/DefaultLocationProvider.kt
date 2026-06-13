package com.dendron.easyweather.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.dendron.easyweather.domain.location.LocationData
import com.dendron.easyweather.domain.location.LocationProvider
import com.dendron.easyweather.domain.location.LocationResult
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultLocationProvider(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application,
) : LocationProvider {
    override suspend fun getCurrentLocation(): LocationResult {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasAccessFineLocationPermission || !hasAccessCoarseLocationPermission) {
            return LocationResult.PermissionDenied
        }

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            return LocationResult.LocationDisabled
        }

        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        val location = result
                        cont.resume(location?.toLocationResult() ?: LocationResult.Unavailable, onCancellation = null)
                    } else {
                        cont.resume(LocationResult.Unavailable, onCancellation = null)
                    }
                    return@suspendCancellableCoroutine
                }

                addOnSuccessListener { location ->
                    cont.resume(location?.toLocationResult() ?: LocationResult.Unavailable, onCancellation = null)
                }
                addOnFailureListener {
                    cont.resume(LocationResult.Unavailable, onCancellation = null)
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }
}

private fun Location.toLocationResult(): LocationResult = LocationResult.Success(
    LocationData(
        latitude = latitude,
        longitude = longitude,
    ),
)
