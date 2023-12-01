package sitec_it.ru.androidapp.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    //@FormUrlEncoded
    //@POST("hs/service/test")
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("hs/service/test")
    suspend fun getTest(@Header("Authorization")  credentials: String): Response<String>
    //suspend fun getTest(@Field("username")  username:String, @Field("password")  password:String): Response<String>
}