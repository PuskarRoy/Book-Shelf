package com.example.bookshelf.di

import com.example.bookshelf.network.BookApi
import com.example.bookshelf.repository.Repository
import com.example.bookshelf.screen.home.FireRepository
import com.example.bookshelf.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBookApi(): BookApi {
        return Retrofit.Builder().baseUrl(Constant.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build().create(BookApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(bookApi: BookApi) : Repository{
        return Repository(bookApi)
    }

    @Singleton
    @Provides
    fun provideFireRepository() : FireRepository{
        return FireRepository()
    }
}