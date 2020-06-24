package hu.luciferi.foursquarevenuelister

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import hu.luciferi.foursquarevenuelister.repositories.RetrofitRepository
import hu.luciferi.foursquarevenuelister.retrofit.model.SearchData
import java.io.Serializable

class VenueViewModel : ViewModel(), Serializable {
    val defaultLat = -34.0
    val defaultLng = 151.0

    var searchData : LiveData<List<SearchData>> = RetrofitRepository.getSearchData(defaultLat, defaultLng)



    var location: Location? = null
}