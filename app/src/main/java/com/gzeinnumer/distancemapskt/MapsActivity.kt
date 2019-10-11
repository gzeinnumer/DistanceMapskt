package com.gzeinnumer.distancemapskt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Toast
import android.annotation.SuppressLint
import android.util.Log
import java.text.DecimalFormat
import com.google.maps.android.SphericalUtil
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var place1: MarkerOptions? = null
    var place2: MarkerOptions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        place1 = MarkerOptions().position(LatLng(-0.9137739, 100.4640976)).title("Location 1")
        place2 = MarkerOptions().position(LatLng(-0.9030493, 100.3959947)).title("Location 2")

        val param1 = LatLng(place1?.position?.latitude!!, place1?.position?.longitude!!)
        val param2 = LatLng(place2?.position?.latitude!!, place2?.position?.longitude!!)

        val temp = CalculationByDistance(param1, param2)

        val distance = SphericalUtil.computeDistanceBetween(param1, param2)

        Toast.makeText(this, formatNumber(distance), Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        mMap.addMarker(place1)
        mMap.addMarker(place2)
        val camera = LatLng(place1?.position?.latitude!!, place1?.position?.longitude!!)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(camera))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, 12.0f))
    }

    @SuppressLint("DefaultLocale")
    private fun formatNumber(distance: Double?): String {
        var distance = distance
        var unit = "m"
        if (distance!! < 1) {
            distance *= 1000
            unit = "mm"
        } else if (distance > 1000) {
            distance /= 1000
            unit = "km"
        }

        return String.format("%4.3f%s", distance, unit)
    }

    private fun CalculationByDistance(StartP: LatLng, EndP: LatLng): Double {
        val Radius = 6371// radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) + (cos(Math.toRadians(lat1))
                * cos(Math.toRadians(lat2)) * sin(dLon / 2)
                * sin(dLon / 2))
        val c = 2 * asin(sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec = Integer.valueOf(newFormat.format(meter))
        Log.i(
            "Radius Value", "" + valueResult + "   KM  " + kmInDec
                    + " Meter   " + meterInDec
        )

        return Radius * c
    }
}
