package sitec_it.ru.androidapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject
@HiltViewModel
class LicenseSettingsViewModel@Inject constructor(private val repository: Repository): ViewModel()  {
    private val licenseMutableLiveData: MutableLiveData<ProfileLicense?> = MutableLiveData()
    val user: LiveData<ProfileLicense?>
        get() = licenseMutableLiveData



    fun initView(){
        viewModelScope.launch(Dispatchers.IO) {
            val id = repository.getProfileFromSP()
            val foundProfileLicense = repository.getProfileLicense(id)
            if(foundProfileLicense!=null){
                licenseMutableLiveData.postValue(foundProfileLicense)
            }else{
                licenseMutableLiveData.postValue(null)
            }
        }
    }

    fun updateProfile(profileLicense: ProfileLicense){
        viewModelScope.launch(Dispatchers.IO) { repository.updateProfileLicense(profileLicense) }
    }
}