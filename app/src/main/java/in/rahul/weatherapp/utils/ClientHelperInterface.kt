package `in`.rahul.weatherapp.utils

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ClientHelperInterface {

//    http://api.openweathermap.org/data/2.5/forecast?q=Bangalore&appid=
    @GET("forecast")
   fun getForcast(@Query("q") city:String, @Query("appid") appid:String): Call<JsonElement>

    companion object {
        var retrofit:Retrofit? = null
        fun create(): ClientHelperInterface {
            if (retrofit == null){
                retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ApiUrlHelper.BaseUrl).build()
            }
            return retrofit?.create(ClientHelperInterface::class.java)!!
        }
    }
}