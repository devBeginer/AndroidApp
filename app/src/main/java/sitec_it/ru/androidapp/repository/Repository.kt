package sitec_it.ru.androidapp.repository

import dagger.hilt.android.scopes.ViewModelScoped
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.network.NetworkHelper
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(private val localRepository: LocalRepository, private val remoteRepository: RemoteRepository, private val networkHelper: NetworkHelper) {

    suspend fun updateProfile(profile: Profile) = localRepository.updateProfile(profile)
    suspend fun insertProfile(profile: Profile) = localRepository.insertProfile(profile)
    suspend fun getProfile(name: String) = localRepository.getProfile(name)
    suspend fun updateProfileLicense(profileLicense: ProfileLicense) = localRepository.updateProfileLicense(profileLicense)
    suspend fun insertProfileLicense(profileLicense: ProfileLicense) = localRepository.insertProfileLicense(profileLicense)
    suspend fun getProfileLicense(id: Long) = localRepository.getProfileLicenseByProfile(id)
    fun getProfileFromSP() = localRepository.getCurrentProfileIdFromSP()
    fun saveProfileToSP(id: Long) = localRepository.saveCurrentProfileIdToSP(id)
    suspend fun getTestFromApi( username: String,  password: String): String?{
        return if(networkHelper.isNetworkConnected()){
            remoteRepository.getTestFromApi(username, password)
        }else {
            null
        }
    }

}