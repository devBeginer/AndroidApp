package sitec_it.ru.androidapp.data.models

data class ProfileSpinnerItem(val id: Long = 0,
                              var name: String,
                              var base: String,
                              var server: String,
                              var ssl: Boolean,
                              var port: String,
                              var login: String,
                              var password: String){
    override fun toString(): String {
        return name
    }
}
