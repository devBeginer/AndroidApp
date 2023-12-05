package sitec_it.ru.androidapp.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
@Entity(
    foreignKeys = [ForeignKey(
        entity = Profile::class,
        parentColumns = ["id"], childColumns = ["profile"], onDelete = CASCADE
    )]
)
data class ProfileLicense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var profile: Long,
    var server: String,
    var port: String,
    var login: String,
    var password: String
)
