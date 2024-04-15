package com.test.unsplashdemoapp.persistency

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.unsplashdemoapp.models.ImageRoot
import com.test.unsplashdemoapp.models.RemoteKeys

@Database(entities = arrayOf(ImageRoot::class, RemoteKeys::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imagesDao(): ImagesDAO
    abstract fun remoteKeysDao(): RemoteKeysDAO
}