package com.example.gogreen.di

import com.example.gogreen.api.ApiInterface
import com.example.gogreen.ui.history.HistoryRepo
import com.example.gogreen.ui.home.HomeRepo
import com.example.gogreen.ui.stationinfo.StationInfoRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideBaseUrl(): String{
        return "http://20.241.108.184:3004/"
    }

    @Provides
    fun getRetroFitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClients())
            .build()
    }

    @Provides
    fun httpLogingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)

    }

    @Provides
    fun provideOkHttpClients(): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(httpLogingInterceptor())
            .readTimeout(45, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(45, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideApiInterFace(): ApiInterface {
        return getRetroFitInstance().create(ApiInterface::class.java)
    }

/*    @Provides
    fun provideNewsRepository(apiInterface: ApiInterface): LoginRepo {
        return LoginRepo(apiInterface)
    }

    @Provides
    fun provideDummyRepository(apiInterface: ApiInterface): DummyRepo {
        return DummyRepo(apiInterface)
    }*/

    @Provides
    fun provideHomeRepo(apiInterface: ApiInterface): HomeRepo {
        return HomeRepo(apiInterface)
    }

    @Provides
    fun provideStationInfoRepo(apiInterface: ApiInterface): StationInfoRepo {
        return StationInfoRepo(apiInterface)
    }

    @Provides
    fun provideHistoryRepo(apiInterface: ApiInterface): HistoryRepo {
        return HistoryRepo(apiInterface)
    }
}