package hu.luciferi.foursquarevenuelister.ui.main.details

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import hu.luciferi.foursquarevenuelister.repositories.VenuesRepository
import hu.luciferi.foursquarevenuelister.retrofit.model.FSPhoto
import hu.luciferi.foursquarevenuelister.retrofit.model.PhotosData
import hu.luciferi.foursquarevenuelister.retrofit.model.SearchData

class VenueDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val name : String? = savedStateHandle.get<String>("name")
    val address : String? = savedStateHandle.get<String>("address")
    val distance : Int? = savedStateHandle.get<Int>("distance")
    val id : String? = savedStateHandle.get<String>("id")//"4b7d16d1f964a5204bae2fe3"
    var imageIndex = -1
    var imageSrc : Drawable? = null
    var isBtnPressed = false

    var photos : LiveData<List<FSPhoto>> = MutableLiveData<List<FSPhoto>>()//VenuesRepository.getVenuePhotos(id)
    fun downloadPhotos() {
        Log.d("retrofit", id ?: "empty")
        photos = VenuesRepository.getVenuePhotos(id)
    }
}