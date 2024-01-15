package sitec_it.ru.androidapp.di.modules

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sitec_it.ru.androidapp.data.AppDatabase
import sitec_it.ru.androidapp.data.dao.FormDao
import sitec_it.ru.androidapp.data.dao.MessageListDao
import sitec_it.ru.androidapp.data.dao.NodeDao
import sitec_it.ru.androidapp.data.dao.OrganizationDao
import sitec_it.ru.androidapp.data.dao.ProfileDao
import sitec_it.ru.androidapp.data.dao.ProfileLicenseDao
import sitec_it.ru.androidapp.data.dao.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        app: Application
    ): AppDatabase = Room
        .databaseBuilder(app, AppDatabase::class.java, "applicationDatabase")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideProfileDao(db: AppDatabase): ProfileDao = db.profileDao()

    @Provides
    @Singleton
    fun provideProfileLicenseDao(db: AppDatabase): ProfileLicenseDao = db.profileLicenseDao()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideNodeDao(db: AppDatabase): NodeDao = db.nodeDao()

    @Provides
    @Singleton
    fun provideMessageListDao(db: AppDatabase): MessageListDao = db.messageListDao()


    @Provides
    @Singleton
    fun provideOrganizationDao(db: AppDatabase): OrganizationDao = db.organizationDao()


    @Provides
    @Singleton
    fun provideFormDao(db: AppDatabase): FormDao = db.formDao()


    /*@Provides
    @Singleton
    fun provideElementDao(db: AppDatabase): ElementDao = db.elementDao()


    @Provides
    @Singleton
    fun provideArgumentDao(db: AppDatabase): ArgumentDao = db.argumentDao()


    @Provides
    @Singleton
    fun provideActionDao(db: AppDatabase): ActionDao = db.actionDao()*/
}