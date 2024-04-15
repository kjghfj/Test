package com.test.unsplashdemoapp.di

import com.test.unsplashdemoapp.network.UnsplashAPI
import com.test.unsplashdemoapp.persistency.AppDatabase
import com.test.unsplashdemoapp.persistency.FavoritesDataBase
import com.test.unsplashdemoapp.repositories.UnsplashRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelsModule {

    @Provides
    @ViewModelScoped
    fun providesUnsplashRepository(api: UnsplashAPI, database: AppDatabase, favoritesDataBase: FavoritesDataBase) = UnsplashRepository(database, api, favoritesDataBase)
}