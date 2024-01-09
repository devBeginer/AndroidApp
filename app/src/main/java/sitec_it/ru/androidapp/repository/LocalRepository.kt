package sitec_it.ru.androidapp.repository

import android.content.SharedPreferences
import sitec_it.ru.androidapp.SharedPreferencesUtils.editPref
import sitec_it.ru.androidapp.data.dao.ActionDao
import sitec_it.ru.androidapp.data.dao.ArgumentDao
import sitec_it.ru.androidapp.data.dao.ElementDao
import sitec_it.ru.androidapp.data.dao.FormDao
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
import sitec_it.ru.androidapp.data.models.form.ActionDB
import sitec_it.ru.androidapp.data.models.form.ArgumentDB
import sitec_it.ru.androidapp.data.models.form.ElementDB
import sitec_it.ru.androidapp.data.models.form.FormDB
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
    private val formDao: FormDao,
    private val elementDao: ElementDao,
    private val argumentDao: ArgumentDao,
    private val actionDao: ActionDao
) {
    companion object {
        const val PROFILE_ID = "profile_id"
        const val USER_CODE = "user_code"
        const val CurrentDatabaseId = "CurrentDatabaseId"
        const val FIRST_APP_START = "firstStartApp"
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


    fun isFirstAppStartFromSP(): Boolean {
        return sharedPreferences.getBoolean(FIRST_APP_START, true)
    }

    fun saveFirstAppStartToSP(isFirstStart: Boolean) {
        return sharedPreferences.editPref(FIRST_APP_START, isFirstStart)
    }

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
    suspend fun getUserByDbId(databaseId: String): List<User> {
        return userDao.getUsersByDbId(databaseId)
    }
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
    suspend fun getOrganization(code: String, databaseId: String): OrganizationDB? {
        return organizationDao.getOrganization(code, databaseId)
    }
    suspend fun getOrganizationListByBdId(databaseId: String): List<OrganizationDB> {
        return organizationDao.getOrganizationListByDbId(databaseId)
    }
    suspend fun updateOrganization(organizationDB: OrganizationDB) = organizationDao.updateOrganization(organizationDB)
    suspend fun insertOrganization(organizationDB: OrganizationDB) = organizationDao.insertOrganization(organizationDB)
    suspend fun deleteOrganization(organizationDB: OrganizationDB) = organizationDao.deleteOrganization(organizationDB)




    suspend fun updateForm(formDB: FormDB) = formDao.updateForm(formDB)
    suspend fun insertForm(formDB: FormDB) {
        val form = formDao.getRecordById(getCurrentDatabaseId(), formDB.formID)
        if(form==null){
            formDao.insertForm(formDB)
        }else{
            formDao.updateForm(formDB)
        }
    }
    suspend fun deleteForm(formDB: FormDB) = formDao.deleteForm(formDB)
    suspend fun getForm(formId: String) = formDao.getRecordById(getCurrentDatabaseId(), formId)
    suspend fun getAllForms() = formDao.getAllForms()




    suspend fun updateElement(elementDB: ElementDB) = elementDao.updateElement(elementDB)
    suspend fun insertElement(elementDB: ElementDB) {
        val element = elementDao.getRecordById(getCurrentDatabaseId(), elementDB.elementID)
        if(element==null){
            elementDao.insertElement(elementDB)
        }else{
            elementDao.updateElement(element)
        }
    }
    suspend fun deleteElement(elementDB: ElementDB) = elementDao.deleteElement(elementDB)
    suspend fun getElement(formId: String) = elementDao.getRecordByForm(getCurrentDatabaseId(), formId)




    suspend fun updateArgument(argumentDB: ArgumentDB) = argumentDao.updateArgument(argumentDB)
    suspend fun insertArgument(argumentDB: ArgumentDB) {
        val argument = argumentDao.getRecordById(getCurrentDatabaseId(), argumentDB.actionName, argumentDB.name, argumentDB.value, argumentDB.elementID)
        if(argument==null){
            argumentDao.insertArgument(argumentDB)
        }else{
            argumentDao.updateArgument(argumentDB)
        }
    }
    suspend fun deleteArgument(argumentDB: ArgumentDB) = argumentDao.deleteArgument(argumentDB)
    suspend fun getArgument(action: String) = argumentDao.getRecordByAction(getCurrentDatabaseId(), action)




    suspend fun updateAction(actionDB: ActionDB) = actionDao.updateAction(actionDB)
    suspend fun insertAction(actionDB: ActionDB) {
        val action = actionDao.getRecordById(getCurrentDatabaseId(), actionDB.actionName, actionDB.elementID)
        if(action==null){
            actionDao.insertAction(actionDB)
        }else{
            actionDao.updateAction(actionDB)
        }
    }
    suspend fun deleteAction(actionDB: ActionDB) = actionDao.deleteAction(actionDB)
    suspend fun getAction(elementId: String) = actionDao.getActionsByElement(getCurrentDatabaseId(), elementId)


}