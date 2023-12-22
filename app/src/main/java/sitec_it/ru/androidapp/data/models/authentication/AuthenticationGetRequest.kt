package sitec_it.ru.androidapp.data.models.authentication

data class AuthenticationGetRequest(
    var login: String,
    val password: String
)