package sitec_it.ru.androidapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var base: String,
    var server: String,
    var ssl: Boolean,
    var notCheckCertificate: Boolean = false,
    var port: String,
    var login: String,
    var password: String,
    var url: String
)
