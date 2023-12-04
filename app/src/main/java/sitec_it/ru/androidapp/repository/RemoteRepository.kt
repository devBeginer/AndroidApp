package sitec_it.ru.androidapp.repository

import okhttp3.Credentials
import sitec_it.ru.androidapp.network.ApiService
import sitec_it.ru.androidapp.network.NetworkHelper
import java.nio.charset.Charset
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val apiService: ApiService, private val networkHelper: NetworkHelper){
    suspend fun getTestFromApi( username: String,  password: String) = networkHelper.safeApiCall(
        //call = { apiService.getTest(username, password) },
        call = { apiService.getTest(Credentials.basic(username, password, Charset.forName("UTF-8"))) },
        errorMessage = "Error Fetching User"
    )
}