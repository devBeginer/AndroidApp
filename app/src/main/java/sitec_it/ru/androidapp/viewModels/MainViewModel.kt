package sitec_it.ru.androidapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.User
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    /*private val userMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val user: LiveData<String?>
        get() = userMutableLiveData*/


    private val userMutableLiveData: MutableLiveData<User?> = MutableLiveData(null)
    val user: LiveData<User?>
        get() = userMutableLiveData


    private val apiResultMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val apiResult: LiveData<String?>
        get() = apiResultMutableLiveData


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
                if(foundApiResult!=null){
                    apiResultMutableLiveData.postValue(foundApiResult)
                }else{
                    apiResultMutableLiveData.postValue(null)
                }
                repository.saveUserToSP(foundUser.login)
                userMutableLiveData.postValue(foundUser)
            }else{
                userMutableLiveData.postValue(null)
            }


        }
    }

    fun prepopulateUser(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(User(login = "login1"))
            repository.insertUser(User(login = "login2"))
            repository.insertUser(User(login = "login3"))
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