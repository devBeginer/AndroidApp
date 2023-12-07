package sitec_it.ru.androidapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.ProfileSpinnerItem
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class BaseSettingsViewModel @Inject constructor(private val repository: Repository): ViewModel()  {
    private val profileMutableLiveData: MutableLiveData<Profile?> = MutableLiveData()
    val user: LiveData<Profile?>
        get() = profileMutableLiveData


    private val profileListMutableLiveData: MutableLiveData<List<ProfileSpinnerItem>> = MutableLiveData()
    val profileList: LiveData<List<ProfileSpinnerItem>>
        get() = profileListMutableLiveData






    fun initView(){
        viewModelScope.launch(Dispatchers.IO) {
            val foundProfile = repository.getProfileById(repository.getProfileFromSP())
            val foundProfileList = repository.getProfileList()
            if(foundProfile!=null){
                repository.saveProfileToSP(foundProfile.id)
                profileMutableLiveData.postValue(foundProfile)
            }else{
                profileMutableLiveData.postValue(null)
            }
            if(foundProfileList!=null){
                profileListMutableLiveData.postValue(foundProfileList.map { profile -> ProfileSpinnerItem(
                    id = profile.id,
                    name = profile.name,
                    base = profile.base,
                    server  = profile.server,
                    ssl = profile.ssl,
                    port = profile.port,
                    login = profile.login,
                    password = profile.password
                )
                })
            }

        }
    }

    fun initView(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            val foundProfile = repository.getProfileById(id)
            if(foundProfile!=null){
                repository.saveProfileToSP(foundProfile.id)
                profileMutableLiveData.postValue(foundProfile)
            }else{
                profileMutableLiveData.postValue(null)
            }
        }
    }

    fun updateProfile(profile: Profile){
        viewModelScope.launch(Dispatchers.IO) { repository.updateProfile(profile) }

    }

    fun createProfile(name: String, onInsert: ()->Unit, onExist: ()->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            if(repository.getProfile(name)==null){
                val id: Long = repository.insertProfile(
                    Profile(
                        name = name,
                        base = "TMP_Test",
                        server = "192.168.1.0",
                        ssl = false,
                        port = "8080",
                        login = "test",
                        password = "test"
                    )
                )

                val newProfile = repository.getProfileById(id)

                if (newProfile != null) {
                    repository.saveProfileToSP(newProfile.id)
                    repository.insertProfileLicense(
                        ProfileLicense(
                            profile = newProfile.id,
                            server = "192.168.1.0",
                            port = "8080",
                            login = "test",
                            password = "test"
                        )
                    )
                }
                initView()
                withContext(Dispatchers.Main){
                    onInsert()
                }

            }else{
                withContext(Dispatchers.Main){
                    onExist()
                }
            }
        }
    }

    fun deleteProfile(profile: Profile){
        viewModelScope.launch(Dispatchers.IO) {
            if(repository.getProfileById(profile.id)!=null){

                repository.deleteProfile(profile)

                initView()

            }else{

            }
        }
    }
}