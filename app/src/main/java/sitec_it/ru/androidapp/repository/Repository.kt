package sitec_it.ru.androidapp.repository

import dagger.hilt.android.scopes.ViewModelScoped
import sitec_it.ru.androidapp.data.models.Node
import sitec_it.ru.androidapp.data.models.NodeRequest
import sitec_it.ru.androidapp.data.models.NodeResponse
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.User
import sitec_it.ru.androidapp.data.models.UserResponse
import sitec_it.ru.androidapp.data.models.changes.Changes
import sitec_it.ru.androidapp.data.models.message.MessageList
import sitec_it.ru.androidapp.network.NetworkHelper
import java.io.IOException
import javax.inject.Inject

import sitec_it.ru.androidapp.network.Result

@ViewModelScoped
class Repository @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val networkHelper: NetworkHelper
) {

    suspend fun updateProfile(profile: Profile) = localRepository.updateProfile(profile)
    suspend fun insertProfile(profile: Profile): Long = localRepository.insertProfile(profile)
    suspend fun deleteProfile(profile: Profile) = localRepository.deleteProfile(profile)
    suspend fun getProfile(name: String) = localRepository.getProfile(name)
    suspend fun getProfileById(id: Long) = localRepository.getProfileById(id)
    suspend fun getProfileList() = localRepository.getProfileList()
    suspend fun getProfileCount() = localRepository.getProfileCount()


    suspend fun updateNode(node: Node) = localRepository.updateNode(node)
    suspend fun insertNode(node: Node) = localRepository.insertNode(node)
    suspend fun deleteNode(node: Node) = localRepository.deleteNode(node)


    suspend fun updateUser(user: User) = localRepository.updateUser(user)
    suspend fun insertUser(user: User): Long = localRepository.insertUser(user)
    suspend fun deleteUser(user: User) = localRepository.deleteUser(user)
    suspend fun getUser(login: String) = localRepository.getUser(login)
    suspend fun getAllUsers() = localRepository.getAllUsers()

    suspend fun updateProfileLicense(profileLicense: ProfileLicense) =
        localRepository.updateProfileLicense(profileLicense)

    suspend fun insertProfileLicense(profileLicense: ProfileLicense) =
        localRepository.insertProfileLicense(profileLicense)

    suspend fun getProfileLicense(id: Long) = localRepository.getProfileLicenseByProfile(id)


    fun getProfileFromSP() = localRepository.getCurrentProfileIdFromSP()
    fun saveProfileToSP(id: Long) = localRepository.saveCurrentProfileIdToSP(id)


    fun getUserFromSP() = localRepository.getCurrentUserCodeFromSP()
    fun saveUserToSP(code: String) = localRepository.saveCurrentUserCodeToSP(code)


    suspend fun getTestFromApi(urlPostfix: String): Result<String> {
        val currentProfile =
            localRepository.getProfileById(localRepository.getCurrentProfileIdFromSP())
        return if (networkHelper.isNetworkConnected() && currentProfile != null) {
            remoteRepository.getTestFromApi(
                currentProfile.login,
                currentProfile.password,
                currentProfile.url + urlPostfix,
                "Error test api",
                isDisableCheckCertificate()
            )
        } else {
            Result.Error(
                0,
                "Error test api",
                "ERROR - Connection",
                IOException("Error Fetching User, ERROR - Connection")
            )
        }
    }


    suspend fun postNodeToApi(
        username: String,
        password: String,
        url: String,
        nodeRequest: NodeRequest
    ): Result<NodeResponse?> {
        return if (networkHelper.isNetworkConnected()) {
            remoteRepository.postNodeToApi(
                username,
                password,
                url,
                nodeRequest,
                "Error register node",
                isDisableCheckCertificate()
            )
        } else {
            Result.Error(
                0,
                "Error register node",
                "ERROR - Connection",
                IOException("Error register node, ERROR - Connection")
            )
        }
    }

    suspend fun getUsersList(urlPostfix: String): Result<UserResponse?> {
        val currentProfile =
            localRepository.getProfileById(localRepository.getCurrentProfileIdFromSP())
        if (networkHelper.isNetworkConnected() && currentProfile != null) {
            return remoteRepository.loadUsers(
                currentProfile.login,
                currentProfile.password,
                currentProfile.url + urlPostfix,
                "Error Fetching Users",
                isDisableCheckCertificate()
            )

        } else {
            return Result.Error(
                0,
                "Error Fetching Users",
                "ERROR - Connection",
                IOException("Error Fetching Users, ERROR - Connection")
            )
        }
    }

    private suspend fun isDisableCheckCertificate(): Boolean {
        return localRepository.getProfileById(getProfileFromSP())
            ?.let { profile -> profile.notCheckCertificate && profile.ssl } ?: false
    }

    suspend fun getUserByCode(code: String): User? {
        return localRepository.getUserByCode(code)
    }

    suspend fun getChanges():Result<Changes?> {
        val currentProfile =
            localRepository.getProfileById(localRepository.getCurrentProfileIdFromSP())
        var dataBody = localRepository.getLastMessage()
        if (dataBody == null){
            dataBody = MessageList(localRepository.getCurrentDatabaseId().toString(),"0")
        }
        if (networkHelper.isNetworkConnected() && currentProfile != null) {
            return remoteRepository.getChanges(
                currentProfile.login,
                currentProfile.password,
                currentProfile.url + "hs/MobileClient/changes",
                "Error Fetching Users",
                isDisableCheckCertificate(),
                dataBody
            )

        } else {
            return Result.Error(
                0,
                "Error Fetching Users",
                "ERROR - Connection",
                IOException("Error Fetching Users, ERROR - Connection")
            )
        }
    }

    fun saveCurrentDatabaseId(nodeId: String) {
        localRepository.saveCurrentDatabaseId(nodeId)
    }
}