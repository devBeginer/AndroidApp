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
                        base = "TMP_Test",
                        server = "192.168.1.0",
                        ssl = false,
                        port = "8080",
                        login = "test",
                        password = "test"
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
}