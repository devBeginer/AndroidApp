package sitec_it.ru.androidapp.repository

import sitec_it.ru.androidapp.data.dao.ProfileDao
import sitec_it.ru.androidapp.data.models.Profile
import javax.inject.Inject

class LocalRepository @Inject constructor(private val profileDao: ProfileDao) {
    suspend fun updateProfile(profile: Profile) = profileDao.updateProfile(profile)
    suspend fun insertProfile(profile: Profile) = profileDao.insertProfile(profile)
    suspend fun getProfile(name: String) = profileDao.getProfileByName(name)
}