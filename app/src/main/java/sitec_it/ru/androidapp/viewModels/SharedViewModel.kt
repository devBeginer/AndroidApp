package sitec_it.ru.androidapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.DialogParams
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

    private val dialogMutableLiveData: MutableLiveData<DialogParams> = MutableLiveData()
    val dialog: LiveData<DialogParams>
        get() = dialogMutableLiveData

    private val scanResultMutableLiveData: MutableLiveData<String?> = MutableLiveData(null)
    val scanResult: LiveData<String?>
        get() = scanResultMutableLiveData

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


    fun postScanResult(result: String?){
        scanResultMutableLiveData.postValue(result)
        //scanResult = result
    }
    fun isFirstStartApp(): Boolean{
        return repository.isFirstAppStartFromSP()
    }


    fun markNotFirstAppStart(){
        repository.saveFirstAppStartToSP(false)
    }
}