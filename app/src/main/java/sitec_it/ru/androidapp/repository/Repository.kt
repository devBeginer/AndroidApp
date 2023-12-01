package sitec_it.ru.androidapp.repository

import dagger.hilt.android.scopes.ViewModelScoped
import sitec_it.ru.androidapp.data.dao.UserDao
import sitec_it.ru.androidapp.data.models.User
import sitec_it.ru.androidapp.network.ApiService
import sitec_it.ru.androidapp.network.NetworkHelper
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(val apiService: ApiService, val userDao: UserDao, val networkHelper: NetworkHelper) {

    suspend fun getUserByLogin(login: String) = userDao.getUserByLogin(login)
    suspend fun insertUser(user: User) = userDao.insertUser(user)

}