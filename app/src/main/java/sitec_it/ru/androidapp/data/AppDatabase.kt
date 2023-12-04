package sitec_it.ru.androidapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import sitec_it.ru.androidapp.data.dao.ProfileDao
import sitec_it.ru.androidapp.data.models.Profile

@Database(
    entities = [Profile::class],
    version = 2, exportSchema = false
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun profileDao(): ProfileDao
}