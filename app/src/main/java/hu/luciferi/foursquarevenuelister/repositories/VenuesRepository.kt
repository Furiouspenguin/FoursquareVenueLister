package hu.luciferi.foursquarevenuelister.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hu.luciferi.foursquarevenuelister.retrofit.model.FSPhoto
import hu.luciferi.foursquarevenuelister.retrofit.model.MetaPhotosResponse
import hu.luciferi.foursquarevenuelister.retrofit.model.MetaSearchResponse
import hu.luciferi.foursquarevenuelister.retrofit.model.SearchData
import hu.luciferi.foursquarevenuelister.retrofit.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object VenuesRepository {

    private var searchData : List<SearchData>? = null

    fun getSearchData(lat : Double, lng : Double) : LiveData<List<SearchData>> {
        val data = MutableLiveData<List<SearchData>>()
        //így a dátum mindig a lekérdezésnél lesz kiszámolva - ha a RetrofitClient object (singleton), akkor a dátumot minden lekérésnél kell definiálni újabb Quary-ként (v)
        val retrofitClient = RetrofitClient()
        retrofitClient.api.getVenueSearchLL("${lat},${lng}").enqueue(object : Callback<MetaSearchResponse>{
            override fun onFailure(call: Call<MetaSearchResponse>, t: Throwable) {
                Log.e("retrofit search","VenueSearch failed", t)
                data.value = searchData
            }

            override fun onResponse(
                call: Call<MetaSearchResponse>,
                response: Response<MetaSearchResponse>
            ) {
                if (response.isSuccessful){
                    data.value = response.body()?.response?.venues
                    searchData = data.value
                }
                else {
                    Log.e("retrofit search",response.errorBody()?.string() ?: response.code().toString())
                    data.value = searchData
                }
            }
        })
        return data
    }




    fun getVenuePhotos(id : String?) : LiveData<List<FSPhoto>> {
        val data = MutableLiveData<List<FSPhoto>>()
        val retrofitClient = RetrofitClient()

        if (id != null) {
            retrofitClient.api.getVenuePhotos(id).enqueue(object : Callback<MetaPhotosResponse>{
                override fun onFailure(call: Call<MetaPhotosResponse>, t: Throwable) {
                    Log.e("retrofit photos","VenuePhotos failed", t)
                }

                override fun onResponse(
                    call: Call<MetaPhotosResponse>,
                    response: Response<MetaPhotosResponse>
                ) {
                    Log.d("retrofit photos", response.toString())
                    if (response.isSuccessful){
                        data.value = response.body()?.response?.photos?.items
                    }
                    else {
                        Log.e("retrofit photos",response.errorBody()?.string() ?: response.code().toString())
                    }
                }

            })
        }


        return data
    }
}