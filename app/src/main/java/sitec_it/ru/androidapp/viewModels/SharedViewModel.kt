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

    fun initData(){
        viewModelScope.launch(Dispatchers.IO) {
            profileCountMutableLiveData.postValue(repository.getProfileCount())
        }
    }
}