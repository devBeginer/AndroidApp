package sitec_it.ru.androidapp.data.models.user

data class UserSpinner(val code: String,
                       val login: String,
                       val name: String,
                       val password: String){
    override fun toString(): String {
        return name
    }
}
