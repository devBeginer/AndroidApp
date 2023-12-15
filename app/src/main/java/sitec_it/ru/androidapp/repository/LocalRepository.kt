package sitec_it.ru.androidapp.repository

import android.content.SharedPreferences
import sitec_it.ru.androidapp.SharedPreferencesUtils.editPref
import sitec_it.ru.androidapp.data.dao.NodeDao
import sitec_it.ru.androidapp.data.dao.ProfileDao
import sitec_it.ru.androidapp.data.dao.ProfileLicenseDao
import sitec_it.ru.androidapp.data.dao.UserDao
import sitec_it.ru.androidapp.data.models.Node
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.User
import javax.inject.Inject

class LocalRepository @Inject constructor(private val profileDao: ProfileDao,
                                          private val profileLicenseDao: ProfileLicenseDao,
                                          private val userDao: UserDao,
                                          private val nodeDao: NodeDao,
                                          private val sharedPreferences: SharedPreferences) {
    companion object{
        const val PROFILE_ID = "profile_id"
        const val USER_CODE = "user_code"
    }



    suspend fun updateProfile(profile: Profile) = profileDao.updateProfile(profile)
    suspend fun insertProfile(profile: Profile): Long = profileDao.insertProfile(profile)
    suspend fun deleteProfile(profile: Profile) = profileDao.deleteProfile(profile)
    suspend fun getProfile(name: String) = profileDao.getProfileByName(name)
    suspend fun getProfileById(id: Long) = profileDao.getProfileById(id)
    suspend fun getProfileList() = profileDao.getAllProfile()
    suspend fun getProfileCount(): Int {
        return profileDao.getAllProfile().count()
    }


    suspend fun updateNode(node: Node) = nodeDao.updateNode(node)
    suspend fun insertNode(node: Node) = nodeDao.insertNode(node)
    suspend fun deleteNode(node: Node) = nodeDao.deleteNode(node)



    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    suspend fun getUser(login: String) = userDao.getUserByLogin(login)
    suspend fun getAllUsers() = userDao.getAllUsers()




    suspend fun updateProfileLicense(profileLicense: ProfileLicense) = profileLicenseDao.updateProfileLicense(profileLicense)
    suspend fun insertProfileLicense(profileLicense: ProfileLicense) = profileLicenseDao.insertProfileLicense(profileLicense)
    suspend fun getProfileLicense(id: Long) = profileLicenseDao.getProfileLicenseById(id)
    suspend fun getProfileLicenseByProfile(id: Long) = profileLicenseDao.getProfileLicenseByProfile(id)



    fun getCurrentProfileIdFromSP(): Long{
        return sharedPreferences.getLong(PROFILE_ID, 1)
    }

    fun saveCurrentProfileIdToSP(id: Long){
        return sharedPreferences.editPref(PROFILE_ID, id)
    }



    fun getCurrentUserCodeFromSP(): String{
        return sharedPreferences.getString(USER_CODE, "") ?: ""
    }

    fun saveCurrentUserCodeToSP(login: String){
        return sharedPreferences.editPref(USER_CODE, login)
    }
}