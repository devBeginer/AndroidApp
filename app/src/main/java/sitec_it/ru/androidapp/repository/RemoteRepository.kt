package sitec_it.ru.androidapp.repository

import okhttp3.Credentials
import sitec_it.ru.androidapp.data.models.NodeRequest
import sitec_it.ru.androidapp.di.modules.NormalApiService
import sitec_it.ru.androidapp.di.modules.SSlFactoryApiService
import sitec_it.ru.androidapp.network.ApiService
import sitec_it.ru.androidapp.network.NetworkHelper
import java.nio.charset.Charset
import javax.inject.Inject

class RemoteRepository @Inject constructor(@NormalApiService private val apiService: ApiService, @SSlFactoryApiService private val apiServiceSSLFactory: ApiService, private val networkHelper: NetworkHelper){
    /*suspend fun getTestFromApi( username: String,  password: String, disableCheckCertificate: Boolean) = networkHelper.safeApiCall(
        //call = { apiService.getTest(username, password) },
        call = { apiService.getTest(Credentials.basic(username, password, Charset.forName("UTF-8"))) },
        errorMessage = "Error Fetching User"
    )*/

    suspend fun getTestFromApi( username: String,  password: String, url: String, disableCheckCertificate: Boolean) = networkHelper.safeApiCall(
        //call = { apiService.getTest(username, password) },
        call = {
            (if(disableCheckCertificate) apiServiceSSLFactory else apiService)
                .getTest(Credentials.basic(username, password, Charset.forName("UTF-8")), url)
               },
        errorMessage = "Error Fetching User"
    )

    suspend fun postNodeToApi( username: String,  password: String, url: String, nodeRequest: NodeRequest, disableCheckCertificate: Boolean) = networkHelper.safeApiCall(
        call = {
            (if(disableCheckCertificate) apiServiceSSLFactory else apiService)
                .postNode(Credentials.basic(username, password, Charset.forName("UTF-8")), url, nodeRequest)
               },
        errorMessage = "Error register node"
    )
}