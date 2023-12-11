package sitec_it.ru.androidapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url
import sitec_it.ru.androidapp.data.models.NodeRequest
import sitec_it.ru.androidapp.data.models.NodeResponse

interface ApiService {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET
    suspend fun getTest(@Header("Authorization")  credentials: String, @Url url: String): Response<String>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST
    suspend fun postNode(@Header("Authorization")  credentials: String, @Url url: String, @Body request: NodeRequest): Response<NodeResponse?>


    //suspend fun getTest(@Field("username")  username:String, @Field("password")  password:String): Response<String>
}