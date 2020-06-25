package hu.luciferi.foursquarevenuelister.retrofit.service

import hu.luciferi.foursquarevenuelister.retrofit.model.MetaPhotosResponse
import hu.luciferi.foursquarevenuelister.retrofit.model.MetaSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitApi {

    //SEARCH
    @GET("search")
    fun getVenueSearchLL(@Query("ll") ll : String) : Call<MetaSearchResponse>
    /*
    @GET("search")
    fun getVenueSearchNear(@Query("near") near : String) : Call<MetaSearchResponse>
    //Könnyű teszteléshez hasznos, mivel nem kellenek koordináták és ugyanazt az eredményt adja
*/
    //PHOTOS
    @GET("{VENUE_ID}/photos")
    fun getVenuePhotos(@Path("VENUE_ID") VENUE_ID : String
                       //@Query("limit") limit : Int
    ) : Call<MetaPhotosResponse>
}