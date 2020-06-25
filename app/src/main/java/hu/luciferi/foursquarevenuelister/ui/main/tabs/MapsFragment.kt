package hu.luciferi.foursquarevenuelister.ui.main.tabs

import android.content.Intent
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.luciferi.foursquarevenuelister.R
import hu.luciferi.foursquarevenuelister.VenueViewModel
import hu.luciferi.foursquarevenuelister.ui.main.details.VenueDetailsActivity

class MapsFragment : Fragment() {

    private val viewModel : VenueViewModel by activityViewModels()

    private var map : GoogleMap? = null

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        map = googleMap

        var currentLoc = LatLng(viewModel.location?.latitude ?: viewModel.defaultLat, viewModel.location?.longitude ?: viewModel.defaultLng)
        googleMap.addMarker(MarkerOptions().position(currentLoc).title("Current Location Marker")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))

        viewModel.searchData.observe(viewLifecycleOwner, Observer {
            if (viewModel.searchData.value != null) {
                //mivel ezekre kattintva is meg kell nyitni a hozzájuk tartozó adatlapot, így el kell menteni rajtuk az indexüket,
                //hogy aztán a listából meg tudjuk hívni a szükséges adatokat -> ezért az indexeken kell végigmenni
            for (i in viewModel.searchData.value!!.indices){
                if (viewModel.searchData.value!![i].location.lat != null && viewModel.searchData.value!![i].location.lng != null){
                    currentLoc = LatLng(viewModel.searchData.value!![i].location.lat!!.toDouble(), viewModel.searchData.value!![i].location.lng!!.toDouble())
                    val marker = googleMap.addMarker(MarkerOptions().position(currentLoc).title(viewModel.searchData.value!![i].name))
                    marker.tag = i
                }
            }
        }
        })


        googleMap.setOnInfoWindowClickListener{marker ->
            if (marker.title != "Current Location Marker") {
                val intent = Intent(context, VenueDetailsActivity::class.java).apply {
                    val venue = viewModel.searchData.value!![marker.tag as Int]
                    putExtra("name", venue.name)
                    putExtra("address", venue.location.address)
                    putExtra("distance", venue.location.distance)
                    putExtra("id", venue.id)
                }
                startActivity(intent)
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }


    override fun onResume() {
        super.onResume()
        setMap()
    }

    private fun setMap(){
        map?.let{
            val currentLoc = LatLng(viewModel.location?.latitude ?: viewModel.defaultLat, viewModel.location?.longitude ?: viewModel.defaultLng)
            //it.addMarker(MarkerOptions().position(currentLoc).title("Current Location Marker"))
            it.moveCamera(CameraUpdateFactory.newLatLng(currentLoc))
            it.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
        }
    }
}