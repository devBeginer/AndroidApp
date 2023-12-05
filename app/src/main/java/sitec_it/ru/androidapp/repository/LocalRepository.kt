package sitec_it.ru.androidapp.repository

import android.content.SharedPreferences
import sitec_it.ru.androidapp.SharedPreferencesUtils.editPref
import sitec_it.ru.androidapp.data.dao.ProfileDao
import sitec_it.ru.androidapp.data.dao.ProfileLicenseDao
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import javax.inject.Inject

class LocalRepository @Inject constructor(private val profileDao: ProfileDao, private val profileLicenseDao: ProfileLicenseDao, private val sharedPreferences: SharedPreferences) {
    suspend fun updateProfile(profile: Profile) = profileDao.updateProfile(profile)
    suspend fun insertProfile(profile: Profile) = profileDao.insertProfile(profile)
    suspend fun getProfile(name: String) = profileDao.getProfileByName(name)
    suspend fun updateProfileLicense(profileLicense: ProfileLicense) = profileLicenseDao.updateProfileLicense(profileLicense)
    suspend fun insertProfileLicense(profileLicense: ProfileLicense) = profileLicenseDao.insertProfileLicense(profileLicense)
    suspend fun getProfileLicense(id: Long) = profileLicenseDao.getProfileLicenseById(id)
    suspend fun getProfileLicenseByProfile(id: Long) = profileLicenseDao.getProfileLicenseByProfile(id)

    fun getCurrentProfileIdFromSP(): Long{
        return sharedPreferences.getLong("profile_id", 1)
    }

    fun saveCurrentProfileIdToSP(id: Long){
        return sharedPreferences.editPref("profile_id", id)
    }
}