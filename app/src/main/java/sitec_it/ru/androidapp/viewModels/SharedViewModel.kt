package sitec_it.ru.androidapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.DialogParams
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.Settings
import sitec_it.ru.androidapp.data.models.menu.Form
import sitec_it.ru.androidapp.data.models.profile.Profile
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: Repository): ViewModel()  {
    val menuForms = MutableLiveData<Form>()
    private val profileCountMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val profileList: LiveData<Int>
        get() = profileCountMutableLiveData

    private val pbVisibilityMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val pbVisibility: LiveData<Boolean>
        get() = pbVisibilityMutableLiveData

    private val dialogMutableLiveData: MutableLiveData<DialogParams> = MutableLiveData()
    val dialog: LiveData<DialogParams>
        get() = dialogMutableLiveData

    private val scanResultMutableLiveData: MutableLiveData<Boolean?> = MutableLiveData(null)
    val scanResult: LiveData<Boolean?>
        get() = scanResultMutableLiveData

    private val scanModeMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val scanMode: LiveData<Boolean>
        get() = scanModeMutableLiveData

    //var scanResult: String = ""

    var url: String = ""
    var databaseId: String = ""
    var currentUserName: String = ""
    fun initData(){
        viewModelScope.launch(Dispatchers.IO) {
            profileCountMutableLiveData.postValue(repository.getProfileCount())
        }
    }

    fun updateProgressBar(visibility: Boolean){
        pbVisibilityMutableLiveData.postValue(visibility)
    }


    fun postDialog(dialogParams: DialogParams){
        dialogMutableLiveData.postValue(dialogParams)
    }


    fun postScanResult(result: Boolean?){
        scanResultMutableLiveData.postValue(result)
        //scanResult = result
    }
    fun isFirstStartApp(): Boolean{
        return repository.isFirstAppStartFromSP()
    }


    fun markNotFirstAppStart(){
        repository.saveFirstAppStartToSP(false)
    }

    fun enableScanMode(enable: Boolean){
        scanModeMutableLiveData.postValue(enable)
    }

    fun updateSettings(settings: Settings) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentProfile = repository.getProfile(settings.profileName)
            val profile =
                if (currentProfile != null) {
                    currentProfile.base = settings.base
                    currentProfile.server = settings.server
                    currentProfile.ssl = settings.ssl
                    currentProfile.port = settings.port
                    currentProfile.login = settings.login
                    currentProfile.password = settings.password
                    currentProfile
                } else {
                    Profile(
                        name = settings.profileName,
                        base = settings.base,
                        server = settings.server,
                        ssl = settings.ssl,
                        port = settings.port,
                        login = settings.login,
                        password = settings.password,
                        url = "http://192.168.1.0:8080/TMP_Test/"
                    )
                }
            profile.url = buildProfileUrl(profile)
            val id = repository.updateProfile(profile)
            val newProfile = repository.getProfileById(id)

            if (newProfile != null) {
                repository.saveProfileToSP(newProfile.id)
                val profileLicense = repository.getProfileLicense(id)
                val newProfileLicense = if (profileLicense != null) {
                    profileLicense.server = settings.licenseServer
                    profileLicense.port = settings.licensePort
                    profileLicense.login = settings.licenseLogin
                    profileLicense.password = settings.licensePassword
                    profileLicense
                } else {
                    ProfileLicense(
                        profile = newProfile.id,
                        server = settings.licenseServer,
                        port = settings.licensePort,
                        login = settings.licenseLogin,
                        password = settings.licensePassword
                    )
                }
                repository.updateProfileLicense(
                    newProfileLicense
                )
                scanResultMutableLiveData.postValue(true)
            }
        }

    }

    private fun buildProfileUrl(profile: Profile): String {

        return if (profile.ssl)
            "https://${profile.server}/${profile.base}/hs/MobileClient"
        else {
            if (profile.port.isEmpty())
                "http://${profile.server}/${profile.base}/hs/MobileClient"
            else
                "http://${profile.server}/${profile.base}/hs/MobileClient"
        }
    }
}