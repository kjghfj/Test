package com.test.unsplashdemoapp.persistency

import androidx.room.*
import com.test.unsplashdemoapp.models.ImageRoot
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFav(image: ImageRoot)

    @Delete
    suspend fun removeFav(image: ImageRoot)

    @Query("SELECT * FROM image_root")
    fun observeFavsChanges(): Flow<List<ImageRoot>>
}