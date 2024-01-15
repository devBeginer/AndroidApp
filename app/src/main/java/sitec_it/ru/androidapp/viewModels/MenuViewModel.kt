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

  /*  fun loadForms(){
        viewModelScope.launch(Dispatchers.IO) {
            //val response = repository.getForms()
            val response = repository.getNewForms()
            when(response){
                is Result.Success -> {
                    Log.d("getForms","data ->>> ${response.data}")
                    Log.d("getForms","формы загружены успешно ->> сохранение forms")

                    //save forms
                    //repository.deleteOldForms()
                    repository.saveForms(response.data)
                    Log.d("getForms"," ->> формы сохранены")
                    val mainForm = response.data.Forms.find { form -> form.FormName == "Главное меню" }

                    menuFormsMutableLiveData.postValue(mainForm*//*response.data.Forms[0]*//*)
                }
                is Result.Error -> {
                    Log.d("getForms", response.errorStringFormatLong())
                    menuFormsMutableLiveData.postValue(null)
                }
            }


        }
    }*/

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
                        Log.d("changes","Сохранение данных начато")

                        repository.updateMessage(dataBody.MessageNumber)
                        repository.saveOrganizations(dataBody.Organization)
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

    fun getSubMenuForm(formId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(2500)
           // val form = repository.getFormById(formId)

        }
    }

}