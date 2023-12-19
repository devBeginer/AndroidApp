package sitec_it.ru.androidapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
    @PrimaryKey
    val code: String,
    val login: String,
    val name: String,
    val password: String = "",
    val databaseID: String = ""

)
