package com.test.unsplashdemoapp.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.test.unsplashdemoapp.models.ImageRoot
import com.test.unsplashdemoapp.network.UnsplashAPI
import com.test.unsplashdemoapp.persistency.AppDatabase
import com.test.unsplashdemoapp.persistency.FavoritesDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UnsplashRepository @Inject constructor(
    private val database: AppDatabase,
    private val api: UnsplashAPI,
    private val favoritesDataBase: FavoritesDataBase
    ) {

    @OptIn(ExperimentalPagingApi::class)
    fun observeNewsListPaginated(pageSize: Int): Pager<Int, ImageRoot> {
        return Pager(
            config = PagingConfig(pageSize, enablePlaceholders = true),
            remoteMediator = UnsplashRemoteMediator(pageSize, database, api)
        ) {
            database.imagesDao().observeNewsPaginated()
        }
    }

    fun observeFavs(coroutineScope: CoroutineScope) = favoritesDataBase
        .favoritesDAO()
        .observeFavsChanges()
        .distinctUntilChanged()
        .shareIn(coroutineScope, started = SharingStarted.Eagerly)

    suspend fun getImage(imageId: String) = api.getImage(imageId)

    suspend fun addFavorite(imageRoot: ImageRoot) = withContext(Dispatchers.IO) {
        favoritesDataBase.favoritesDAO().addFav(imageRoot)
    }

    suspend fun removeFavorite(imageRoot: ImageRoot) = withContext(Dispatchers.IO) {
        favoritesDataBase.favoritesDAO().removeFav(imageRoot)
    }
}