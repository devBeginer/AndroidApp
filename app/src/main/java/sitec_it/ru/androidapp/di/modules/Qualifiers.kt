package sitec_it.ru.androidapp.di.modules

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SSlFactoryOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SSlFactoryRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SSlFactoryApiService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalApiService