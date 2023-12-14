package sitec_it.ru.androidapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.User
import sitec_it.ru.androidapp.network.Result
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    /*private val userMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val user: LiveData<String?>
        get() = userMutableLiveData*/


    private val userListMutableLiveData: MutableLiveData<List<User>> = MutableLiveData(null)
    val userList: LiveData<List<User>>
        get() = userListMutableLiveData


    private val userMutableLiveData: MutableLiveData<User?> = MutableLiveData(null)
    val user: LiveData<User?>
        get() = userMutableLiveData


    private val apiResultMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val apiResult: LiveData<String?>
        get() = apiResultMutableLiveData


    private val apiErrorMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val apiError: LiveData<String?>
        get() = apiErrorMutableLiveData


    private val loginMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val login: LiveData<String?>
        get() = loginMutableLiveData


    /*fun initView(){
        userMutableLiveData.postValue(null)
    }*/

    fun login(login: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            /*val foundUser = repository.userDao.getUserByLogin(login)
            if(foundUser!=null && foundUser.password.equals(password)){*/
            val foundUser = repository.getUser(login)
            if(foundUser!=null){
                val foundApiResult = repository.getTestFromApi("hs/service/test")
                when(foundApiResult){
                    is Result.Success->apiResultMutableLiveData.postValue(foundApiResult.data)
                    is Result.Error->apiErrorMutableLiveData.postValue(foundApiResult.toString())
                }
                /*if(foundApiResult!=null){
                    apiResultMutableLiveData.postValue(foundApiResult)
                }else{
                    apiResultMutableLiveData.postValue(null)
                }*/
                repository.saveUserToSP(foundUser.login)
                userMutableLiveData.postValue(foundUser)
            }else{
                userMutableLiveData.postValue(null)
            }


        }
    }

    fun loadUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getUsersList("hs/MobileClient/users")
            when(response){
                is Result.Success-> {
                    val responseList = response.data?.let { it.users } ?: mutableListOf()
                    val usersList = responseList.map { userResponse -> User(userResponse.code, userResponse.login, userResponse.name, userResponse.password) }
                    usersList.forEach {user-> repository.insertUser(user) }
                    userListMutableLiveData.postValue(usersList)
                }
                is Result.Error->apiErrorMutableLiveData.postValue(response.toString())
            }
        }
    }

    fun prepopulateUser(){
        viewModelScope.launch(Dispatchers.IO) {
            //repository.insertUser(User(login = "login1"))
            //repository.insertUser(User(login = "login2"))
            //repository.insertUser(User(login = "login3"))
        }
    }
    fun initLoginField(){
        viewModelScope.launch(Dispatchers.IO) {
            val login = repository.getUserFromSP()
            if (login!=""){
                loginMutableLiveData.postValue(login)
            }
        }
    }

}