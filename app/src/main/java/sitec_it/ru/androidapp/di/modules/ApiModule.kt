package sitec_it.ru.androidapp.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import sitec_it.ru.androidapp.network.ApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @SSlFactoryApiService
    @Provides
    @Singleton
    fun provideSSLFactoryApiService(@SSlFactoryRetrofit retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @NormalApiService
    @Provides
    @Singleton
    fun provideNormalApiService(@NormalRetrofit retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}