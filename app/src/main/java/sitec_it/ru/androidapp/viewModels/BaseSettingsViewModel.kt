package sitec_it.ru.androidapp.viewModels

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sitec_it.ru.androidapp.data.models.Node
import sitec_it.ru.androidapp.data.models.NodeRequest
import sitec_it.ru.androidapp.data.models.NodeResponse
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.ProfileSpinnerItem
import sitec_it.ru.androidapp.network.Result
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject


@HiltViewModel
class BaseSettingsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val profileMutableLiveData: MutableLiveData<Profile?> = MutableLiveData()
    val profile: LiveData<Profile?>
        get() = profileMutableLiveData


    private val profileListMutableLiveData: MutableLiveData<List<ProfileSpinnerItem>> =
        MutableLiveData()
    val profileList: LiveData<List<ProfileSpinnerItem>>
        get() = profileListMutableLiveData


    private val nodeResponseMutableLiveData: MutableLiveData<NodeResponse?> = MutableLiveData(null)
    val nodeResponse: LiveData<NodeResponse?>
        get() = nodeResponseMutableLiveData
    private val apiErrorMutableLiveData: MutableLiveData<String> = MutableLiveData(null)
    val apiError: LiveData<String>
        get() = apiErrorMutableLiveData
    var url: String = ""

    fun initView() {
        viewModelScope.launch(Dispatchers.IO) {
            val foundProfile = repository.getProfileById(repository.getProfileFromSP())
            val foundProfileList = repository.getProfileList()
            if (foundProfile != null) {
                repository.saveProfileToSP(foundProfile.id)
                profileMutableLiveData.postValue(foundProfile)
            } else {
                profileMutableLiveData.postValue(null)
            }
            if (foundProfileList != null) {
                profileListMutableLiveData.postValue(foundProfileList.map { profile ->
                    ProfileSpinnerItem(
                        id = profile.id,
                        name = profile.name,
                        base = profile.base,
                        server = profile.server,
                        ssl = profile.ssl,
                        port = profile.port,
                        login = profile.login,
                        password = profile.password
                    )
                })
            }


        }
    }

    fun initView(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val foundProfile = repository.getProfileById(id)
            if (foundProfile != null) {
                repository.saveProfileToSP(foundProfile.id)
                profileMutableLiveData.postValue(foundProfile)
            } else {
                profileMutableLiveData.postValue(null)
            }
        }
    }

    fun updateProfile(profile: Profile, rebuildUrl: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            profile.url = buildProfileUrl(profile)
            repository.updateProfile(profile)

            //sharedViewModel.buildUrl(profile.id)
            //buildUrl(profile.id)
        }

    }

    fun createProfile(name: String, onInsert: () -> Unit, onExist: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.getProfile(name) == null) {
                val id: Long = repository.insertProfile(
                    Profile(
                        name = name,
                        base = "TMP_Test",
                        server = "192.168.1.0",
                        ssl = false,
                        port = "8080",
                        login = "test",
                        password = "test",
                        url = "http://192.168.1.0:8080/TMP_Test/"
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
                withContext(Dispatchers.Main) {
                    onInsert()
                }

            } else {
                withContext(Dispatchers.Main) {
                    onExist()
                }
            }
        }
    }

    fun deleteProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.getProfileById(profile.id) != null) {

                repository.deleteProfile(profile)

                initView()

            } else {

            }
        }
    }

    fun postNode() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentProfile = profile.value

            if(currentProfile!=null){
                val response = repository.postNodeToApi(
                    //"Админ",
                    currentProfile.login,
                    //"",
                    currentProfile.password,
                    //"http://localhost/WMSLite/hs/MobileClient/registerNode",
                    //url + "registerNode",
                    currentProfile.url + "hs/MobileClient/registerNode",
                    NodeRequest(deviceName())
                )
                when (response){
                    is Result.Success->{
                        val data = response.data
                        if(data!=null){
                            repository.insertNode(Node(
                                nodeId = data.nodeId,
                                prefix = data.prefix
                            ))
                            nodeResponseMutableLiveData.postValue(data)
                        }
                    }
                    is Result.Error->apiErrorMutableLiveData.postValue(response.exception.message)
                }
                /*if (response != null) {

                } else {
                    nodeResponseMutableLiveData.postValue(null)
                }*/
            }
        }
    }

    private fun deviceName(): String {
        val model = Build.MODEL
        val brand = Build.BRAND
        return if (model.toLowerCase().contains(brand.toLowerCase())) {
            model
        } else {
            "$brand+$model"
        }
    }

    private fun buildProfileUrl(profile: Profile): String {
        //val profile = repository.getProfileById(id)

        return if (profile.ssl)
            "https://${profile.server}/${profile.base}/"
        else {
            if (profile.port.isEmpty())
                "http://${profile.server}/${profile.base}/"
            else
                "http://${profile.server}/${profile.base}/"
        }
    }
}