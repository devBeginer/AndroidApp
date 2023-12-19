package sitec_it.ru.androidapp.repository

import android.content.SharedPreferences
import sitec_it.ru.androidapp.SharedPreferencesUtils.editPref
import sitec_it.ru.androidapp.data.dao.MessageListDao
import sitec_it.ru.androidapp.data.dao.NodeDao
import sitec_it.ru.androidapp.data.dao.OrganizationDao
import sitec_it.ru.androidapp.data.dao.ProfileDao
import sitec_it.ru.androidapp.data.dao.ProfileLicenseDao
import sitec_it.ru.androidapp.data.dao.UserDao
import sitec_it.ru.androidapp.data.models.node.Node
import sitec_it.ru.androidapp.data.models.profile.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.user.User
import sitec_it.ru.androidapp.data.models.changes.OrganizationDB
import sitec_it.ru.androidapp.data.models.message.MessageList
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val profileDao: ProfileDao,
    private val profileLicenseDao: ProfileLicenseDao,
    private val userDao: UserDao,
    private val nodeDao: NodeDao,
    private val sharedPreferences: SharedPreferences,
    private val messageListDao: MessageListDao,
    private val organizationDao: OrganizationDao,
) {
    companion object {
        const val PROFILE_ID = "profile_id"
        const val USER_CODE = "user_code"
        const val CurrentDatabaseId = "CurrentDatabaseId"
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


    suspend fun updateProfileLicense(profileLicense: ProfileLicense) =
        profileLicenseDao.updateProfileLicense(profileLicense)

    suspend fun insertProfileLicense(profileLicense: ProfileLicense) =
        profileLicenseDao.insertProfileLicense(profileLicense)

    suspend fun getProfileLicense(id: Long) = profileLicenseDao.getProfileLicenseById(id)
    suspend fun getProfileLicenseByProfile(id: Long) =
        profileLicenseDao.getProfileLicenseByProfile(id)


    fun getCurrentProfileIdFromSP(): Long {
        return sharedPreferences.getLong(PROFILE_ID, 1)
    }

    fun saveCurrentProfileIdToSP(id: Long) {
        return sharedPreferences.editPref(PROFILE_ID, id)
    }


    fun getCurrentUserCodeFromSP(): String {
        return sharedPreferences.getString(USER_CODE, "") ?: ""
    }

    fun saveCurrentUserCodeToSP(login: String) {
        return sharedPreferences.editPref(USER_CODE, login)
    }

    suspend fun getUserByCode(code: String): User? = userDao.getUserByCode(code)
    fun getLastMessage(): MessageList? {

        return messageListDao.getRecordById(getCurrentDatabaseId())
    }

    fun saveCurrentDatabaseId(nodeId: String) {
        sharedPreferences.editPref(CurrentDatabaseId,nodeId)
    }
    fun getCurrentDatabaseId(): String {
        return sharedPreferences.getString(CurrentDatabaseId,"") ?: ""
    }


    /*suspend fun getChangesByDbId(uniqueDbId: String): ChangesDB? {
        return changesDao.getChangesByDbId(uniqueDbId)
    }*/
    suspend fun updateMessage(messageList: MessageList) = messageListDao.updateMessage(messageList)
    suspend fun insertMessage(messageList: MessageList) = messageListDao.insertMessage(messageList)
    suspend fun deleteMessage(messageList: MessageList) = messageListDao.deleteMessage(messageList)
    /*suspend fun updateChanges(changesDB: ChangesDB) = changesDao.updateChange(changesDB)
    suspend fun insertChanges(changesDB: ChangesDB) = changesDao.insertChange(changesDB)
    suspend fun deleteChanges(changesDB: ChangesDB) = changesDao.deleteChange(changesDB)*/
    suspend fun getOrganization(code: String, databaseId: String): OrganizationDB? {
        return organizationDao.getOrganization(code, databaseId)
    }
    suspend fun getOrganizationListByBdId(databaseId: String): List<OrganizationDB> {
        return organizationDao.getOrganizationListByDbId(databaseId)
    }
    suspend fun updateOrganization(organizationDB: OrganizationDB) = organizationDao.updateOrganization(organizationDB)
    suspend fun insertOrganization(organizationDB: OrganizationDB) = organizationDao.insertOrganization(organizationDB)
    suspend fun deleteOrganization(organizationDB: OrganizationDB) = organizationDao.deleteOrganization(organizationDB)
}