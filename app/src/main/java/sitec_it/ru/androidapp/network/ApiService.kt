package sitec_it.ru.androidapp.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url
import sitec_it.ru.androidapp.data.models.authentication.AuthenticationGetRequest
import sitec_it.ru.androidapp.data.models.node.NodeRequest
import sitec_it.ru.androidapp.data.models.node.NodeResponse
import sitec_it.ru.androidapp.data.models.user.UserResponse
import sitec_it.ru.androidapp.data.models.changes.Changes
import sitec_it.ru.androidapp.data.models.message.MessageList

interface ApiService {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET
    suspend fun getTest(
        @Header("Authorization") credentials: String,
        @Url url: String
    ): Response<String>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST
    suspend fun postNode(
        @Header("Authorization") credentials: String,
        @Url url: String,
        @Body request: NodeRequest
    ): Response<NodeResponse?>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET
    suspend fun loadUsers(
        @Header("Authorization") credentials: String,
        @Url url: String
    ): Response<UserResponse?>

    @POST
    suspend fun getChanges(
        @Header("Authorization") credentials: String,
        @Url url: String,
        @Body request: MessageList
    ): Response<Changes?>

    @POST
   suspend fun authenticationUser(
        @Header("Authorization") basic: String,
        @Url url: String,
        @Body dataBody: AuthenticationGetRequest
    ): Response<ResponseBody>

}