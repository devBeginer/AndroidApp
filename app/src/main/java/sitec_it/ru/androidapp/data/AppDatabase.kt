package sitec_it.ru.androidapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import sitec_it.ru.androidapp.data.dao.ProfileDao
import sitec_it.ru.androidapp.data.dao.ProfileLicenseDao
import sitec_it.ru.androidapp.data.dao.UserDao
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.User

@Database(
    entities = [Profile::class, ProfileLicense::class, User::class],
    version = 3, exportSchema = false
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun profileDao(): ProfileDao
    abstract fun profileLicenseDao(): ProfileLicenseDao
    abstract fun userDao(): UserDao
}