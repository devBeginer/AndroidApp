package sitec_it.ru.androidapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    var name: String,
    var login: String,
    var password: String
)
