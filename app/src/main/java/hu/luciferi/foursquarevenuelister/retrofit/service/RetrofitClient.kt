package hu.luciferi.foursquarevenuelister.retrofit.service


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    private val client_id = "L5PH0MR5OFCUVIXS1AGVLRPJSREBS3TFVFE1IGEEFVRBIHBW"
    private val client_secret = "DERCBTAWOUHOOZIAG35NGQUS4VRE4AX2C2AOSZPSJLFL3H0C"

    private val client = OkHttpClient.Builder()
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest = chain.request()
                val originalUrl = originalRequest.url()

                //dátum beállítása
                val date = Calendar.getInstance().time
                val df = SimpleDateFormat("yyyyMMdd", Locale.GERMANY)

                val newUrl = originalUrl.newBuilder()
                    .addQueryParameter("client_id", client_id)
                    .addQueryParameter("client_secret", client_secret)
                    .addQueryParameter("v", df.format(date))
                    .build()

                val requestBuilder = originalRequest.newBuilder().url(newUrl)
                val newRequest = requestBuilder.build()

                return chain.proceed(newRequest)
            }
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.foursquare.com/v2/venues/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api : RetrofitApi = retrofit.create(RetrofitApi::class.java)
}