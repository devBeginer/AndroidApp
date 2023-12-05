package sitec_it.ru.androidapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val userMutableLiveData: MutableLiveData<String?> = MutableLiveData()
    val user: LiveData<String?>
        get() = userMutableLiveData


    fun initView(){
        userMutableLiveData.postValue(null)
    }

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
            repository.insertProfile(Profile(
                name = "По умолчанию",
                base = "WMS_TMP_Test",
                server = "dev2.sitec24.ru",
                ssl = false,
                port = "9090",
                login = "web",
                password = "web"
            ))
        }
    }
}