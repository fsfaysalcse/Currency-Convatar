package com.faysal.currencycon.di

import com.faysal.currencycon.data.CurrencyApi
import com.faysal.currencycon.main.DefaultMainRepository
import com.faysal.currencycon.main.MainRepository
import com.faysal.currencycon.util.Constants
import com.faysal.currencycon.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi() : CurrencyApi = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api : CurrencyApi) : MainRepository = DefaultMainRepository(api)

    @Singleton
    @Provides
    fun providesDispatchers() : DispatcherProvider = object : DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }
}