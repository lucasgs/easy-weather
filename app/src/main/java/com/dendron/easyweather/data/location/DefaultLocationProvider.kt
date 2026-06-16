package com.dendron.easyweather.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.dendron.easyweather.domain.location.CurrentLocationFailure
import com.dendron.easyweather.domain.location.CurrentLocationResult
import com.dendron.easyweather.domain.location.LocationData
import com.dendron.easyweather.domain.location.LocationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultLocationProvider(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application,
) : LocationProvider {
    override suspend fun getCurrentLocation(): CurrentLocationResult {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasAccessFineLocationPermission || !hasAccessCoarseLocationPermission) {
            return CurrentLocationResult.Failure(CurrentLocationFailure.PermissionDenied)
        }

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            return CurrentLocationResult.Failure(CurrentLocationFailure.LocationDisabled)
        }

        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        val location = result
                        cont.resume(location?.toLocationResult() ?: unavailable(), onCancellation = null)
                    } else {
                        cont.resume(unavailable(), onCancellation = null)
                    }
                    return@suspendCancellableCoroutine
                }

                addOnSuccessListener { location ->
                    cont.resume(location?.toLocationResult() ?: unavailable(), onCancellation = null)
                }
                addOnFailureListener {
                    cont.resume(unavailable(), onCancellation = null)
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }
}

private fun unavailable() = CurrentLocationResult.Failure(CurrentLocationFailure.Unavailable)

private fun Location.toLocationResult(): CurrentLocationResult = CurrentLocationResult.Success(
    LocationData(
        latitude = latitude,
        longitude = longitude,
    ),
)
