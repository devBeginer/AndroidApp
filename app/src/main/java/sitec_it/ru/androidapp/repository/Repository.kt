package sitec_it.ru.androidapp.repository

import android.util.Log
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.ResponseBody
import sitec_it.ru.androidapp.data.models.node.Node
import sitec_it.ru.androidapp.data.models.node.NodeRequest
import sitec_it.ru.androidapp.data.models.node.NodeResponse
import sitec_it.ru.androidapp.data.models.profile.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.authentication.AuthenticationGetRequest
import sitec_it.ru.androidapp.data.models.user.User
import sitec_it.ru.androidapp.data.models.user.UserResponse
import sitec_it.ru.androidapp.data.models.changes.Changes
import sitec_it.ru.androidapp.data.models.changes.Organization
import sitec_it.ru.androidapp.data.models.changes.OrganizationDB
import sitec_it.ru.androidapp.data.models.form.ActionDB
import sitec_it.ru.androidapp.data.models.form.ArgumentDB
import sitec_it.ru.androidapp.data.models.form.ElementDB
import sitec_it.ru.androidapp.data.models.form.FormDB
import sitec_it.ru.androidapp.data.models.message.MessageList
import sitec_it.ru.androidapp.data.models.newForms1.Action
import sitec_it.ru.androidapp.data.models.newForms1.Argument
import sitec_it.ru.androidapp.data.models.newForms1.Element
import sitec_it.ru.androidapp.data.models.newForms1.Form
import sitec_it.ru.androidapp.data.models.newForms1.Forms
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


    fun saveFirstAppStartToSP(isFirstStart: Boolean) = localRepository.saveFirstAppStartToSP(isFirstStart)
    fun isFirstAppStartFromSP() = localRepository.isFirstAppStartFromSP()

    fun getProfileFromSP() = localRepository.getCurrentProfileIdFromSP()
    fun saveProfileToSP(id: Long) = localRepository.saveCurrentProfileIdToSP(id)


    fun getUserFromSP() = localRepository.getCurrentUserCodeFromSP()
    fun saveUserToSP(code: String) = localRepository.saveCurrentUserCodeToSP(code)

    suspend fun updateMessage(messageList: MessageList) = localRepository.updateMessage(messageList)
    suspend fun insertMessage(messageList: MessageList) = localRepository.insertMessage(messageList)
    suspend fun deleteMessage(messageList: MessageList) = localRepository.deleteMessage(messageList)
    //suspend fun getChangesByDbId(uniqueDbId: String) = localRepository.getChangesByDbId(uniqueDbId)

    suspend fun updateOrganization(organizationDB: OrganizationDB) = localRepository.updateOrganization(organizationDB)
    suspend fun insertOrganization(organizationDB: OrganizationDB) = localRepository.insertOrganization(organizationDB)
    suspend fun deleteOrganization(organizationDB: OrganizationDB) = localRepository.deleteOrganization(organizationDB)
    suspend fun getOrganization(code: String, databaseId: String) = localRepository.getOrganization(code, databaseId)

    suspend fun updateMessage(messageNumber: Int){
        val lastMessage = localRepository.getLastMessage()
        if (lastMessage!=null){
            localRepository.updateMessage(MessageList(lastMessage.DatabaseID, messageNumber))
        }else{
            localRepository.insertMessage(MessageList(localRepository.getCurrentDatabaseId(), messageNumber))
        }
        Log.d("changes","Сообщение обновлено")
    }
    suspend fun saveOrganizations(organizations: List<Organization>){
        /*val lastMessage = localRepository.getLastMessage()
        if (lastMessage!=null){
            localRepository.updateMessage(MessageList(lastMessage.DatabaseID, dataBody.MessageNumber))
        }else{
            localRepository.insertMessage(MessageList(localRepository.getCurrentDatabaseId(), dataBody.MessageNumber))
        }*/

        organizations.forEach { organization ->
            val oldOrganization = localRepository.getOrganization(organization.Code, localRepository.getCurrentDatabaseId())
            if (oldOrganization!=null){
                localRepository.updateOrganization(OrganizationDB(organization.Code, organization.Name, localRepository.getCurrentDatabaseId()))
            }else{
                localRepository.insertOrganization(OrganizationDB(organization.Code, organization.Name, localRepository.getCurrentDatabaseId()))
            }
        }
        Log.d("changes","Организации обновлены")
    }


    suspend fun getTestFromApi(): Result<String> {
        val currentProfile =
            localRepository.getProfileById(localRepository.getCurrentProfileIdFromSP())
        val url = currentProfile?.let { currentProfile.url.substring(0, currentProfile.url.length-13) + "/service/test" } ?: ""
        Log.d("apiCallUrl", url)
        return if (networkHelper.checkOnline() && currentProfile != null) {
            remoteRepository.getTestFromApi(
                currentProfile.login,
                currentProfile.password,
                //currentProfile.url.substring(0, currentProfile.url.length-13) + "/service/test",
                url,
                "Error test api",
                isDisableCheckCertificate()
            )
        } else {
            Result.Error(
                0,
                "Error test api",
                "ERROR - Connection",
                exception = IOException("Error test api, ERROR - Connection")
            )
        }
    }


    suspend fun postNodeToApi(
        username: String,
        password: String,
        nodeRequest: NodeRequest
    ): Result<NodeResponse?> {
        val currentProfile =
            localRepository.getProfileById(localRepository.getCurrentProfileIdFromSP())
        val url = currentProfile?.let { currentProfile.url + "/registerNode" } ?: ""
        Log.d("apiCallUrl", url)
        return if (networkHelper.checkOnline() && currentProfile != null) {
            remoteRepository.postNodeToApi(
                username,
                password,
                //currentProfile.url + "/registerNode",
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
                exception = IOException("Error register node, ERROR - Connection")
            )
        }
    }

    suspend fun getUsersList(): Result<UserResponse?> {
        val currentProfile =
            localRepository.getProfileById(localRepository.getCurrentProfileIdFromSP())
        val url = currentProfile?.let { currentProfile.url + "/users" } ?: ""
        Log.d("apiCallUrl", url)
        if (networkHelper.checkOnline() && currentProfile != null) {
            return remoteRepository.loadUsers(
                currentProfile.login,
                currentProfile.password,
                //currentProfile.url + "/users",
                url,
                "Error Fetching Users",
                isDisableCheckCertificate()
            )

        } else {
            return Result.Error(
                0,
                "Error Fetching Users",
                "ERROR - Connection",
                exception = IOException("Error Fetching Users, ERROR - Connection")
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

    suspend fun getUserByDbId(databaseId: String): List<User> {
        return localRepository.getUserByDbId(databaseId)
    }

    suspend fun getChanges():Result<Changes?> {
        val currentProfile =
            localRepository.getProfileById(localRepository.getCurrentProfileIdFromSP())
        val url = currentProfile?.let { currentProfile.url + "/changes" } ?: ""
        Log.d("apiCallUrl", url)
        //var prevChanges = localRepository.getCurrentDatabaseId()?.let { dbId -> localRepository.getChangesByDbId(dbId) }
        var dataBody = localRepository.getLastMessage()
        dataBody = if (dataBody == null){
            MessageList(localRepository.getCurrentDatabaseId().toString(),0)
        }else{
            MessageList(localRepository.getCurrentDatabaseId().toString(),dataBody.LastReceived)
        }

        if (networkHelper.checkOnline() && currentProfile != null) {
            return remoteRepository.getChanges(
                currentProfile.login,
                currentProfile.password,
                //currentProfile.url + "/changes",
                url,
                "Error get changes",
                isDisableCheckCertificate(),
                dataBody
            )

        } else {
            return Result.Error(
                0,
                "Error get changes",
                "ERROR - Connection",
                exception = IOException("Error get changes, ERROR - Connection")
            )
        }
    }

    fun saveCurrentDatabaseId(nodeId: String) {
        localRepository.saveCurrentDatabaseId(nodeId)
    }
    fun getCurrentDatabaseId() : String {
        return localRepository.getCurrentDatabaseId() ?: ""
    }

    suspend fun authenticationUser(dataBody:AuthenticationGetRequest): Result<ResponseBody> {
        val currentProfile =
            localRepository.getProfileById(localRepository.getCurrentProfileIdFromSP())
        val url = currentProfile?.let { currentProfile.url + "/login" } ?: ""
        Log.d("apiCallUrl", url)
        //val codeUser = localRepository.getCurrentUserCodeFromSP()
        //dataBody.login = localRepository.getUserByCode(codeUser)?.login.toString()
        if (networkHelper.checkOnline() && currentProfile != null) {
            return remoteRepository.authenticationUser(
                currentProfile.login,
                currentProfile.password,
                //currentProfile.url + "/login",
                url,
                "Error authentication",
                isDisableCheckCertificate(),
                dataBody
                )
        } else {
            return Result.Error(
                0,
                "Error authentication users",
                "ERROR - authentication, check connect",
                exception = IOException("Error authentication user, ERROR - Connection")
            )
        }
    }

    /*suspend fun getForms(): Result<Forms> {
        val currentProfile =
            localRepository.getProfileById(localRepository.getCurrentProfileIdFromSP())
        val url = currentProfile?.let { currentProfile.url + "/forms" } ?: ""
        Log.d("apiCallUrl", url)
        if (networkHelper.checkOnline() && currentProfile != null) {
            return remoteRepository.loadForms(
                currentProfile.login,
                currentProfile.password,
                url,
                "Error load forms",
                isDisableCheckCertificate()
                )
        } else {
            return Result.Error(
                0,
                "Error load forms",
                "ERROR - load forms, check connect",
                exception = IOException("Error load forms, ERROR - Connection")
            )
        }
    }*/

    suspend fun getNewForms(): Result<sitec_it.ru.androidapp.data.models.newForms1.Forms> {
        val currentProfile =
            localRepository.getProfileById(localRepository.getCurrentProfileIdFromSP())
        val url = currentProfile?.let { currentProfile.url + "/forms" } ?: ""
        Log.d("apiCallUrl", url)
        if (networkHelper.checkOnline() && currentProfile != null) {
            return remoteRepository.loadNewForms(
                currentProfile.login,
                currentProfile.password,
                url,
                "Error load forms",
                isDisableCheckCertificate()
                )
        } else {
            return Result.Error(
                0,
                "Error load forms",
                "ERROR - load forms, check connect",
                exception = IOException("Error load forms, ERROR - Connection")
            )
        }
    }

    suspend fun getFormById(formId: String): Form? {
        val formDb = localRepository.getForm(formId)
        val elementDb = localRepository.getElement(formId)
        val elements = elementDb.map { element ->
            val action = localRepository.getAction(element.elementID).map { actionDB ->
                val arguments = localRepository.getArgument(actionDB.actionName).map { argumentDB -> Argument(argumentDB.actionName, argumentDB.name, argumentDB.value) }
                Action(actionDB.actionName, arguments, actionDB.elementID)
            }
            Element(action, element.elementID, element.elementName, element.elementType, element.formID, element.nextField)
        }
        return formDb?.let { Form(elements, formDb.formID, formDb.formName) }
    }

    /*suspend fun deleteOldForms() {
        val oldForms = localRepository.getAllForms()
        oldForms.forEach { formDB ->
            localRepository.deleteForm(formDB)
        }
    }*/

    suspend fun saveForms(form: Forms) {
        val dbId = getCurrentDatabaseId()
        form.Forms.forEach {form->
            localRepository.insertForm(FormDB(form.FormID, form.FormName, dbId))
            form.Elements.forEach{element ->

                localRepository.insertElement(ElementDB(/*form.FormID*/element.FormID, element.ElementID, element.ElementName, element.ElementType, element.NextFieldID, dbId))
                element.Actions.forEach { action ->
                    localRepository.insertAction(ActionDB(action.Action, action.ElementID, dbId))
                    action.Arguments.forEach { argument ->
                        localRepository.insertArgument(ArgumentDB(argument.Action, action.ElementID, argument.Name, argument.Value, dbId))
                    }
                }
            }
        }
    }
}