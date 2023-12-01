package sitec_it.ru.androidapp.di.modules

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sitec_it.ru.androidapp.data.AppDatabase
import sitec_it.ru.androidapp.data.dao.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        app: Application
    ): AppDatabase = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "applicationDatabase"
    ).build()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
}