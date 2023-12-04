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
class AdditionSettingsViewModel@Inject constructor(val repository: Repository): ViewModel()  {
    private val profileMutableLiveData: MutableLiveData<Profile?> = MutableLiveData()
    val user: LiveData<Profile?>
        get() = profileMutableLiveData



    fun initView(name: String){
        viewModelScope.launch(Dispatchers.IO) {
            val foundProfile = repository.getProfile(name)
            if(foundProfile!=null){
                profileMutableLiveData.postValue(foundProfile)
            }else{
                profileMutableLiveData.postValue(null)
            }
        }
    }
}