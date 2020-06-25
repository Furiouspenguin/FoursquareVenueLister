package hu.luciferi.foursquarevenuelister

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import hu.luciferi.foursquarevenuelister.repositories.VenuesRepository
import hu.luciferi.foursquarevenuelister.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    private val viewModel : VenueViewModel by viewModels()


    private lateinit var fusedLocationClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //create location services client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (viewModel.location == null) {
            calculateLocation(false)
        }


        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)




        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }



    //permissions & location --> developers guide alapján

    private val PERMISSION_REQUEST = 17

    private fun checkLocationPermissions() : Boolean {
        return (checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
                var allGranted = true
                for (i in permissions.indices){
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                        allGranted = false
                        if (Build.VERSION.SDK_INT >= 23 &&
                                shouldShowRequestPermissionRationale(permissions[i])){
                            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                if (allGranted) getLocation(true)
            }
        }

    //ezt csak akkor hívjuk meg amikor már ellenőriztük az engedélyeket, szóval fölösleges újra itt is lekérdezni
    @SuppressLint("MissingPermission")
    private fun getLocation(forceIt: Boolean){
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (viewModel.location != it || forceIt) {
                viewModel.location = it
                viewModel.refreshSearchData()
                //toastLong("Refreshed data")
            }
        }
    }

    //23as androidtól kezdve kell elkérnie az alkalmazásnak futás közben helyi engedélyeket
    fun calculateLocation(forceIt : Boolean){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkLocationPermissions()) {
                getLocation(forceIt)
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST
                )
            }
        } else {
            getLocation(forceIt)
        }
    }

    //tesztelésre
    private fun AppCompatActivity.toastLong(message : String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}