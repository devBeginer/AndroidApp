package sitec_it.ru.androidapp.repository

import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.Credentials
import retrofit2.http.Field
import sitec_it.ru.androidapp.data.dao.UserDao
import sitec_it.ru.androidapp.data.models.User
import sitec_it.ru.androidapp.network.ApiService
import sitec_it.ru.androidapp.network.NetworkHelper
import java.nio.charset.Charset
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(val apiService: ApiService, val userDao: UserDao, val networkHelper: NetworkHelper) {

    suspend fun getUserByLogin(login: String) = userDao.getUserByLogin(login)
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun getTestFromApi( username: String,  password: String) = networkHelper.safeApiCall(
        //call = { apiService.getTest(username, password) },
        call = { apiService.getTest(Credentials.basic(username, password, Charset.forName("UTF-8"))) },
        errorMessage = "Error Fetching User"
    )

}