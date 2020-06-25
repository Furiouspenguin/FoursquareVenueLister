package hu.luciferi.foursquarevenuelister

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.luciferi.foursquarevenuelister.repositories.VenuesRepository
import hu.luciferi.foursquarevenuelister.retrofit.model.SearchData

class VenueViewModel : ViewModel() {
    val defaultLat = -34.0
    val defaultLng = 151.0
    var searchData : LiveData<List<SearchData>> = MutableLiveData<List<SearchData>>().apply { value = listOf() }
    var location: Location? = null

    fun refreshSearchData(){
        searchData = MutableLiveData()
        searchData = VenuesRepository.getSearchData(location?.latitude ?: defaultLat, location?.longitude ?: defaultLng)
    }
}