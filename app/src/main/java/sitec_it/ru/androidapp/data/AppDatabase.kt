package sitec_it.ru.androidapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import sitec_it.ru.androidapp.data.dao.ChangesDao
import sitec_it.ru.androidapp.data.dao.MessageListDao
import sitec_it.ru.androidapp.data.dao.NodeDao
import sitec_it.ru.androidapp.data.dao.OrganizationDao
import sitec_it.ru.androidapp.data.dao.ProfileDao
import sitec_it.ru.androidapp.data.dao.ProfileLicenseDao
import sitec_it.ru.androidapp.data.dao.UserDao
import sitec_it.ru.androidapp.data.models.Node
import sitec_it.ru.androidapp.data.models.Profile
import sitec_it.ru.androidapp.data.models.ProfileLicense
import sitec_it.ru.androidapp.data.models.User
import sitec_it.ru.androidapp.data.models.changes.ChangesDB
import sitec_it.ru.androidapp.data.models.changes.OrganizationDB
import sitec_it.ru.androidapp.data.models.message.MessageList

@Database(
    entities = [Profile::class, ProfileLicense::class, User::class, Node::class,MessageList::class,ChangesDB::class,OrganizationDB::class],
    version = 10, exportSchema = false
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun profileDao(): ProfileDao
    abstract fun profileLicenseDao(): ProfileLicenseDao
    abstract fun userDao(): UserDao
    abstract fun nodeDao(): NodeDao
    abstract fun messageListDao(): MessageListDao
    abstract fun changesDao(): ChangesDao
    abstract fun organizationDao(): OrganizationDao
}