package sitec_it.ru.androidapp.repository

import dagger.hilt.android.scopes.ViewModelScoped
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.network.NetworkHelper
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(/*val apiService: ApiService, val userDao: UserDao, */private val localRepository: LocalRepository, private val remoteRepository: RemoteRepository, private val networkHelper: NetworkHelper) {

    /*suspend fun getUserByLogin(login: String) = userDao.getUserByLogin(login)
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun getTestFromApi( username: String,  password: String) = networkHelper.safeApiCall(
        //call = { apiService.getTest(username, password) },
        call = { apiService.getTest(Credentials.basic(username, password, Charset.forName("UTF-8"))) },
        errorMessage = "Error Fetching User"
    )*/

    suspend fun updateProfile(profile: Profile) = localRepository.updateProfile(profile)
    suspend fun insertProfile(profile: Profile) = localRepository.insertProfile(profile)
    suspend fun getProfile(name: String) = localRepository.getProfile(name)
    suspend fun getTestFromApi( username: String,  password: String): String?{
        return if(networkHelper.isNetworkConnected()){
            remoteRepository.getTestFromApi(username, password)
        }else {
            null
        }
    }

}