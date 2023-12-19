package sitec_it.ru.androidapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sitec_it.ru.androidapp.data.models.changes.Changes
import sitec_it.ru.androidapp.data.models.changes.ChangesDB
import sitec_it.ru.androidapp.data.models.changes.OrganizationDB
import sitec_it.ru.androidapp.network.Result
import sitec_it.ru.androidapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    val nameForLabel = MutableLiveData<String?>()
    private val changesObserve = MutableLiveData<Changes>()
    val changes: LiveData<Changes>
        get() =  changesObserve

    private val changesError = MutableLiveData<String?>(null)
    val error: LiveData<String?>
        get() =  changesError

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
            delay(5000)
            when(response){
                is Result.Success -> {
                    response.data?.let { dataBody ->
                        /*val currentChanges = repository.getChangesByDbId(repository.getCurrentDatabaseId())
                        val changeId = if(currentChanges!=null){
                            repository.updateChanges(ChangesDB(currentChanges.id, dataBody.IsWait, dataBody.MessageNumber, dataBody.UniqueDbId))
                            currentChanges.id
                        }else{
                            repository.insertChanges(ChangesDB(isWait =  dataBody.IsWait, messageNumber = dataBody.MessageNumber, uniqueDbId = dataBody.UniqueDbId))
                        }
                        val organizations = repository.getOrganizationByChange(changeId)
                        organizations.forEach { organisation->repository.deleteOrganization(organisation) }
                        dataBody.Organization.forEach { organization -> repository.insertOrganization(
                            OrganizationDB(code = organization.Code, name = organization.Name, change = changeId)
                        ) }*/
                        Log.d("changes","Сохранение данных начато")

                        repository.updateMessage(dataBody.MessageNumber)
                        repository.saveOrganizations(dataBody.Organization)
                        //Log.d("changes",dataBody.toString())
                        Log.d("changes","Сохранение данных завершено")
                        changesObserve.postValue(dataBody)
                    }
                }
                is Result.Error -> {
                    Log.d("changes",response.errorStringFormat())
                    changesError.postValue(response.errorStringFormat())
                }
            }
        }
    }

}