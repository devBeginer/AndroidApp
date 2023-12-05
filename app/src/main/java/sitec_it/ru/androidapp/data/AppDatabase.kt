package sitec_it.ru.androidapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import sitec_it.ru.androidapp.data.dao.ProfileDao
import sitec_it.ru.androidapp.data.dao.ProfileLicenseDao
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense

@Database(
    entities = [Profile::class, ProfileLicense::class],
    version = 2, exportSchema = false
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun profileDao(): ProfileDao
    abstract fun profileLicenseDao(): ProfileLicenseDao
}