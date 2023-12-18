package sitec_it.ru.androidapp.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.changes.Changes
import sitec_it.ru.androidapp.network.Result
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val nameForLabel = MutableLiveData<String?>()
    val changesObserve = MutableLiveData<Changes>()

    fun setTvHello() {
        viewModelScope.launch(Dispatchers.IO) {
            val code = repository.getUserFromSP()
            Log.d("user","code -> $code")
            val nameUser = repository.getUserByCode(code)?.name
            Log.d("user","vm -> $nameUser")
            nameForLabel.postValue(nameUser)
        }
    }

    fun getChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            val response =   repository.getChanges()
            when(response){
                is Result.Success -> {
                    response.data?.let { dataBody ->
                        Log.d("changes",dataBody.toString())
                        changesObserve.postValue(dataBody)
                    }
                }
                is Result.Error -> {
                    Log.d("changes",response.description)
                }
            }
        }
    }

}