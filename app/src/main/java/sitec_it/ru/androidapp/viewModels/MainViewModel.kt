package sitec_it.ru.androidapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.User
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repository: Repository): ViewModel() {

    //private val userMutableLiveData: MutableLiveData<User?> = MutableLiveData()
    private val userMutableLiveData: MutableLiveData<String?> = MutableLiveData()
    val user: LiveData<String?>
    //val user: LiveData<User?>
        get() = userMutableLiveData



    fun login(login: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            /*val foundUser = repository.userDao.getUserByLogin(login)
            if(foundUser!=null && foundUser.password.equals(password)){*/
            val foundUser = repository.getTestFromApi(login, password)
            if(foundUser!=null){
                userMutableLiveData.postValue(foundUser)
            }else{
                userMutableLiveData.postValue(null)
            }
        }
    }

    fun prepopulate(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(User(name = "User1", login = "login1", password = "password1"))
            repository.insertUser(User(name = "User2", login = "login2", password = "password2"))
            repository.insertUser(User(name = "User3", login = "login3", password = "password3"))
        }
    }
}