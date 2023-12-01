package sitec_it.ru.androidapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import sitec_it.ru.androidapp.data.dao.UserDao
import sitec_it.ru.androidapp.data.models.User

@Database(
    entities = [User::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun userDao(): UserDao
}