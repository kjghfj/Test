package com.test.unsplashdemoapp.persistency

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.unsplashdemoapp.models.ImageRoot

@Database(entities = arrayOf(ImageRoot::class), version = 2)
abstract class FavoritesDataBase : RoomDatabase() {
    abstract fun favoritesDAO(): FavoritesDAO
}