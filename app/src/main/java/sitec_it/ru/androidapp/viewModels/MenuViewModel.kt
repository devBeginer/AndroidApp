package sitec_it.ru.androidapp.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val nameForLabel = MutableLiveData<String?>()

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
        }
    }

}