package sitec_it.ru.androidapp.repository

import dagger.hilt.android.scopes.ViewModelScoped
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.User
import sitec_it.ru.androidapp.network.NetworkHelper
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(private val localRepository: LocalRepository, private val remoteRepository: RemoteRepository, private val networkHelper: NetworkHelper) {

    suspend fun updateProfile(profile: Profile) = localRepository.updateProfile(profile)
    suspend fun insertProfile(profile: Profile): Long = localRepository.insertProfile(profile)
    suspend fun deleteProfile(profile: Profile) = localRepository.deleteProfile(profile)
    suspend fun getProfile(name: String) = localRepository.getProfile(name)
    suspend fun getProfileById(id: Long) = localRepository.getProfileById(id)
    suspend fun getProfileList() = localRepository.getProfileList()
    suspend fun getProfileCount() = localRepository.getProfileCount()


    suspend fun updateUser(user: User) = localRepository.updateUser(user)
    suspend fun insertUser(user: User): Long = localRepository.insertUser(user)
    suspend fun deleteUser(user: User) = localRepository.deleteUser(user)
    suspend fun getUser(login: String) = localRepository.getUser(login)

    suspend fun updateProfileLicense(profileLicense: ProfileLicense) = localRepository.updateProfileLicense(profileLicense)
    suspend fun insertProfileLicense(profileLicense: ProfileLicense) = localRepository.insertProfileLicense(profileLicense)
    suspend fun getProfileLicense(id: Long) = localRepository.getProfileLicenseByProfile(id)



    fun getProfileFromSP() = localRepository.getCurrentProfileIdFromSP()
    fun saveProfileToSP(id: Long) = localRepository.saveCurrentProfileIdToSP(id)



    fun getUserFromSP() = localRepository.getCurrentUserLoginFromSP()
    fun saveUserToSP(login: String) = localRepository.saveCurrentUserLoginToSP(login)



    suspend fun getTestFromApi( username: String,  password: String): String?{
        return if(networkHelper.isNetworkConnected()){
            remoteRepository.getTestFromApi(username, password, isDisableCheckCertificate())
        }else {
            null
        }
    }


    private suspend fun isDisableCheckCertificate(): Boolean{
        return localRepository.getProfileById(getProfileFromSP())
            ?.let { profile -> profile.notCheckCertificate && profile.ssl } ?: false
    }
}