package hu.luciferi.foursquarevenuelister.retrofit.service

import hu.luciferi.foursquarevenuelister.retrofit.model.PhotosData
import hu.luciferi.foursquarevenuelister.retrofit.model.SearchData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET

interface RetrofitApi {

    //SEARCH
    @GET("/search")
    fun getVenueSearchLL(@Field("ll") ll : String) : Call<SearchData>
    @GET("/search")
    fun getVenueSearchNear(@Field("near") near : String) : Call<SearchData>

    //PHOTOS
    @GET("/{VENUE_ID}/photos")
    fun getVenuePhotos(@Field("VENUE_ID") VENUE_ID : String,
                       @Field("limit") limit : Int) : Call<PhotosData>
}