package sitec_it.ru.androidapp.repository

import okhttp3.Credentials
import sitec_it.ru.androidapp.data.models.authentication.AuthenticationGetRequest
import sitec_it.ru.androidapp.data.models.changes.Changes
import sitec_it.ru.androidapp.data.models.menu.Form
import sitec_it.ru.androidapp.data.models.message.MessageList
import sitec_it.ru.androidapp.data.models.node.NodeRequest
import sitec_it.ru.androidapp.data.models.user.UserResponse
import sitec_it.ru.androidapp.di.modules.NormalApiService
import sitec_it.ru.androidapp.di.modules.SSlFactoryApiService
import sitec_it.ru.androidapp.network.ApiService
import sitec_it.ru.androidapp.network.NetworkHelper
import java.nio.charset.Charset
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    @NormalApiService private val apiService: ApiService,
    @SSlFactoryApiService private val apiServiceSSLFactory: ApiService,
    private val networkHelper: NetworkHelper
) {
    /*suspend fun getTestFromApi( username: String,  password: String, disableCheckCertificate: Boolean) = networkHelper.safeApiCall(
        //call = { apiService.getTest(username, password) },
        call = { apiService.getTest(Credentials.basic(username, password, Charset.forName("UTF-8"))) },
        errorMessage = "Error Fetching User"
    )*/

    suspend fun getTestFromApi(
        username: String,
        password: String,
        url: String,
        errorMessage: String,
        disableCheckCertificate: Boolean
    ) = networkHelper.safeApiCall(
        //call = { apiService.getTest(username, password) },
        call = {
            //(if (disableCheckCertificate) apiServiceSSLFactory else apiService)
            getApiService(disableCheckCertificate)
                .getTest(Credentials.basic(username, password, Charset.forName("UTF-8")), url)
        },
        errorMessage = errorMessage/*"Error Fetching User"*/
    )

    suspend fun postNodeToApi(
        username: String,
        password: String,
        url: String,
        nodeRequest: NodeRequest,
        errorMessage: String,
        disableCheckCertificate: Boolean
    ) = networkHelper.safeApiCall(
        call = {
            //(if (disableCheckCertificate) apiServiceSSLFactory else apiService)
            getApiService(disableCheckCertificate)
                .postNode(
                    Credentials.basic(username, password, Charset.forName("UTF-8")),
                    url,
                    nodeRequest
                )
        },
        errorMessage = errorMessage/*"Error register node"*/
    )

    suspend fun loadUsers(
        username: String,
        password: String,
        url: String,
        errorMessage: String,
        disableCheckCertificate: Boolean
    ): sitec_it.ru.androidapp.network.Result<UserResponse?> = networkHelper.safeApiCall(
        call = {
            //(if (disableCheckCertificate) apiServiceSSLFactory else apiService)
            getApiService(disableCheckCertificate)
                .loadUsers(Credentials.basic(username, password, Charset.forName("UTF-8")), url)
        },
        errorMessage = errorMessage/*"Error register node"*/
    )

    suspend fun getChanges(
        login: String,
        password: String,
        url: String,
        errorMessage: String,
        disableCheckCertificate: Boolean,
        dataBody: MessageList
    ): sitec_it.ru.androidapp.network.Result<Changes?> = networkHelper.safeApiCall(
        call = {
            //(if (disableCheckCertificate) apiServiceSSLFactory else apiService)
            getApiService(disableCheckCertificate)
                .getChanges(
                    Credentials.basic(login, password, Charset.forName("UTF-8")), url,
                    dataBody
                )
        },
        errorMessage = errorMessage
    )

    suspend fun authenticationUser(
        login: String,
        password: String,
        url: String,
        errorMessage: String,
        disableCheckCertificate: Boolean,
        dataBody: AuthenticationGetRequest
    ): sitec_it.ru.androidapp.network.Result<Form> = networkHelper.safeApiCall(
        call = {
            //(if (disableCheckCertificate) apiServiceSSLFactory else apiService)
            getApiService(disableCheckCertificate)
                .authenticationUser(
                    Credentials.basic(login, password, Charset.forName("UTF-8")), url,
                    dataBody
                )
        },
        errorMessage = errorMessage
    )

    suspend fun loadForms(
        login: String,
        password: String,
        url: String,
        errorMessage: String,
        disableCheckCertificate: Boolean
    ) = networkHelper.safeApiCall(
        call = {
            getApiService(disableCheckCertificate)
                .loadForms(
                    Credentials.basic(login, password, Charset.forName("UTF-8")), url
                )
        },
        errorMessage = errorMessage
    )



    private fun getApiService(disableCheckCertificate: Boolean): ApiService{
        return if (disableCheckCertificate) apiServiceSSLFactory else apiService
    }
}