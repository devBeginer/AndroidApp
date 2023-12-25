package sitec_it.ru.androidapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
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
    private val changesObserve = MutableLiveData<Changes>()
    val changes: LiveData<Changes>
        get() =  changesObserve

    private val changesError = MutableLiveData<String?>(null)
    val error: LiveData<String?>
        get() =  changesError

    private val formMutableLiveData = MutableLiveData<ArrayList<Pair<String, String>>>()
    val form: LiveData<ArrayList<Pair<String, String>>>
        get() =  formMutableLiveData

    fun setTvHello() {
        viewModelScope.launch(Dispatchers.IO) {
            val code = repository.getUserFromSP()
            Log.d("user","code -> $code")
            val nameUser = repository.getUserByCode(code)?.name
            Log.d("user","vm -> $nameUser")
            nameForLabel.postValue(nameUser)
        }
    }

    fun loadForms(){
        viewModelScope.launch(Dispatchers.IO) {



        }
    }

    fun getMainForm(){
        viewModelScope.launch(Dispatchers.IO) {

            val menuElements = arrayListOf<Pair<String, String>>(
                Pair("Главное меню","Приемка"),
                Pair("Главное меню","Размещение"),
                Pair("Главное меню","Перемещение"),
                Pair("Главное меню","Отбор"),
                Pair("Главное меню","Отгрузка"),
                Pair("Главное меню","Подпитка"),
                Pair("Главное меню","Регламентные"),
                Pair("Главное меню","Сервисные")
            )

            formMutableLiveData.postValue(menuElements)
        }
    }

    fun getSubMenu(section: String){


        val form: ArrayList<Pair<String, String>> = when (section) {
            "Приемка" -> {

                arrayListOf(
                    Pair("Рабочй поток", "Создать предварительную приемку"),
                    Pair("Рабочй поток", "Предварительная приемка"),
                    Pair("Рабочй поток", "Закрыть предварительную приемку"),
                    Pair("Рабочй поток", "Создать приемку")
                )
            }
            "Размещение" -> {

                arrayListOf(
                    Pair("Рабочй поток", "Универсальное размещение"),
                    Pair("Рабочй поток", "Размещение контейнеров"),
                    Pair("Рабочй поток", "Размещение товара")
                )
            }
            "Перемещение" -> {
                arrayListOf(
                    Pair("Рабочй поток", "Универсальное перемещение"),
                    Pair("Рабочй поток", "Перемещение контейнеров"),
                    Pair("Рабочй поток", "Свободное перемещение контейнеров"),
                    Pair("Рабочй поток", "Перемещение товара")
                )
            }
            "Отбор" -> {
                arrayListOf(
                    Pair("Рабочй поток", "Отбор товара"),
                    Pair("Рабочй поток", "Отбор контейнеров"),
                    Pair("Рабочй поток", "Кластерный отбор"),
                    Pair("Рабочй поток", "Консолидация контейнеров")
                )
            }
            "Отгрузка" -> {
                arrayListOf(
                    Pair("Рабочй поток", "Сортировка"),
                    Pair("Рабочй поток", "Упаковка"),
                    Pair("Рабочй поток", "Контроль отгрузки"),
                    Pair("Рабочй поток", "Отгрузка")
                )
            }
            "Подпитка" -> {
                arrayListOf(
                    Pair("Рабочй поток", "Подпитка контейнеров"),
                    Pair("Рабочй поток", "Подпитка товара")
                )
            }
            "Регламентные" -> {
                arrayListOf(
                    Pair("Рабочй поток", "Контроль качества"),
                    Pair("Рабочй поток", "Инвентаризация"),
                    Pair("Рабочй поток", "Свободная инвентаризация контейнера"),
                    Pair("Рабочй поток", "Маркировка")
                )
            }
            "Сервисные" -> {
                arrayListOf(
                    Pair("Рабочй поток", "Ввод остатков"),
                    Pair("Рабочй поток", "Остатки товаров"),
                    Pair("Рабочй поток", "Ввод вгх"),
                    Pair("Рабочй поток", "Ввод шк")
                )
            }
            else ->{
                arrayListOf()
            }


        }
        formMutableLiveData.postValue(form)
    }

    fun getChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            val response =   repository.getChanges()
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
                    Log.d("changes",response.errorStringFormatLong())
                    changesError.postValue(response.errorStringFormat())
                }
            }
        }
    }

}