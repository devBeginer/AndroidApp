package sitec_it.ru.androidapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(private val repository: Repository): ViewModel()  {
    fun initDefaultProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            if(repository.getProfileCount()==0){
                val id = repository.insertProfile(
                    Profile(
                        name = "По умолчанию",
                        base = "WMS_TMP_Test",
                        server = "dev2.sitec24.ru",
                        ssl = false,
                        port = "9090",
                        login = "web",
                        password = "web"
                    )
                )

                val newProfile = repository.getProfile("По умолчанию")

                if (newProfile != null) {
                    repository.saveProfileToSP(newProfile.id)
                    repository.insertProfileLicense(
                        ProfileLicense(
                            profile = newProfile.id,
                            server = "dev2.sitec24.ru",
                            port = "9090",
                            login = "tsd",
                            password = "sitecmobile"
                        )
                    )
                }
            }
        }
    }
}