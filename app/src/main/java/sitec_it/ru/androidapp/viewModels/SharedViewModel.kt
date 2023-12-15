package sitec_it.ru.androidapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.ProfileSpinnerItem
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: Repository): ViewModel()  {
    private val profileCountMutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val profileList: LiveData<Int>
        get() = profileCountMutableLiveData

    private val pbVisibilityMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val pbVisibility: LiveData<Boolean>
        get() = pbVisibilityMutableLiveData

    var scanResult: String = ""

    var url: String = ""
    fun initData(){
        viewModelScope.launch(Dispatchers.IO) {
            profileCountMutableLiveData.postValue(repository.getProfileCount())
        }
    }

    fun updateProgressBar(visibility: Boolean){
        pbVisibilityMutableLiveData.postValue(visibility)
    }

    suspend fun buildUrl(id: Long){
        val profile = repository.getProfileById(id)
        if(profile!=null){
            url = if (profile.ssl)
                "https://${profile.server}/${profile.base}/hs/MobileClient/"
            else {
                if (profile.port.isEmpty())
                    "http://${profile.server}/${profile.base}/hs/MobileClient/"
                else
                    "http://${profile.server}/${profile.base}/hs/MobileClient/"
            }

            repository
        }
    }

    fun postScanResult(result: String){
        scanResult = result
    }
}