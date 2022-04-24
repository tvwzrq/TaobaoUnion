package com.example.taobaounion.di

import com.example.taobaounion.api.TaobaoUnionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideApi():TaobaoUnionApi{
        return TaobaoUnionApi.create()
    }
}