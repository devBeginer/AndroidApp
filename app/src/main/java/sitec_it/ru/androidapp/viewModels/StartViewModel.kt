package sitec_it.ru.androidapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.profile.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.Settings
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val scanResultMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val scanResult: LiveData<Boolean>
        get() = scanResultMutableLiveData

    fun initDefaultProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.getProfileCount() == 0) {
                val id = repository.insertProfile(
                    Profile(
                        name = "По умолчанию",
                        base = "TMP_Test",
                        server = "192.168.1.0",
                        ssl = false,
                        port = "8080",
                        login = "test",
                        password = "test",
                        url = "http://192.168.1.0:8080/TMP_Test/"
                    )
                )

                val newProfile = repository.getProfile("По умолчанию")

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
            }
        }
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