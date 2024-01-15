package sitec_it.ru.androidapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.hash.HashCode
import com.google.common.hash.HashFunction
import com.google.common.hash.Hashing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.authentication.AuthenticationGetRequest
import sitec_it.ru.androidapp.data.models.menu.Form
import sitec_it.ru.androidapp.data.models.profile.Profile
import sitec_it.ru.androidapp.data.models.user.User
import sitec_it.ru.androidapp.network.Result
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    /*private val userMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val user: LiveData<String?>
        get() = userMutableLiveData*/


    private val userListMutableLiveData: MutableLiveData<List<User>> = MutableLiveData(null)
    val userList: LiveData<List<User>>
        get() = userListMutableLiveData


    private val userMutableLiveData: MutableLiveData<User?> = MutableLiveData(null)
    val user: LiveData<User?>
        get() = userMutableLiveData


    private val profileMutableLiveData: MutableLiveData<Profile> = MutableLiveData(null)
    val profile: LiveData<Profile>
        get() = profileMutableLiveData


    private val apiResultMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val apiResult: LiveData<String?>
        get() = apiResultMutableLiveData


    private val apiErrorMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val apiError: LiveData<String?>
        get() = apiErrorMutableLiveData


    private val loginMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val login: LiveData<String?>
        get() = loginMutableLiveData
    val responseGetLogin = MutableLiveData<Form?>()
    val authenticationUserObserver = MutableLiveData<String>()
    val authenticationUser: LiveData<String>
        get() = authenticationUserObserver

    fun login(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
        val hashPass = getHashingSha256(password)
            val dataBody = AuthenticationGetRequest(login,hashPass)
            val response = repository.authenticationUser(dataBody)
            when(response){
                is Result.Success -> {
                    responseGetLogin.postValue(response.data)
                    Log.d("authentication","data ->>> ${response.data}")
                    Log.d("authentication","авторизация прошла успешно ->> сохранение user")

                    val foundUser = repository.getUser(login)
                    foundUser?.let { repository.saveUserToSP(it.code) }
                    Log.d("authentication"," ->> сохранение меню")
                    // save local menu
                    repository.saveForms(response.data)

                  //  authenticationUserObserver.postValue("ok")
                }
                is Result.Error -> {
                    Log.d("authentication",response.errorStringFormatLong())
                    apiErrorMutableLiveData.postValue("errorAuth")
                }
            }
        }
    }

    fun initProfileName() {
        viewModelScope.launch(Dispatchers.IO) {
            val foundProfile = repository.getProfileById(repository.getProfileFromSP())

            foundProfile?.let { profile: Profile -> profileMutableLiveData.postValue(profile) }

        }
    }

    fun loadUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getUsersList()
            when (response) {
                is Result.Success -> {
                    val responseList = response.data?.let { it.users } ?: mutableListOf()
                    val usersList = responseList.map { userResponse ->
                        User(
                            userResponse.code,
                            userResponse.login,
                            userResponse.name,
                            databaseID = repository.getCurrentDatabaseId()/*,
                            userResponse.password*/
                        )
                    }
                    usersList.forEach { user -> repository.insertUser(user) }
                    userListMutableLiveData.postValue(usersList)
                }

                is Result.Error -> {
                    Log.d("loadUsers",response.errorStringFormatLong())

                    apiErrorMutableLiveData.postValue(response.errorStringFormat())
                }
            }
        }
    }

    fun prepopulateUser() {
        viewModelScope.launch(Dispatchers.IO) {
            //repository.insertUser(User(login = "login1"))
            //repository.insertUser(User(login = "login2"))
            //repository.insertUser(User(login = "login3"))
        }
    }

    fun initLoginField() {
        viewModelScope.launch(Dispatchers.IO) {
            val code = repository.getUserFromSP()
            if (code != "") {
                loginMutableLiveData.postValue(code)
            }
        }
    }

    fun saveUserToSp(codeUser: String?) {
        if (codeUser != null) {
            repository.saveUserToSP(codeUser)
        }
    }

    private fun getHashingSha256(input: String): String {
        val hashFunction: HashFunction = Hashing.sha256()
        val hc: HashCode = hashFunction
            .newHasher()
            .putString(input.trim(), Charsets.UTF_8)
            .hash()
        return hc.toString()
    }
}