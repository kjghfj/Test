package com.test.unsplashdemoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.unsplashdemoapp.common.DEFAULT_PAGE_SIZE
import com.test.unsplashdemoapp.models.ImageRoot
import com.test.unsplashdemoapp.repositories.UnsplashRepository
import com.test.unsplashdemoapp.ui.detail.DetailStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: UnsplashRepository) : ViewModel() {
    private val internalFavsCache = MutableStateFlow(mapOf<String, ImageRoot>())
    val favsCache =    internalFavsCache.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap()
        )

    val imageStateFlow: MutableStateFlow<DetailStates> = MutableStateFlow(DetailStates.Loading(true))

    var job: Job? = null
    init {
         job = viewModelScope.launch {
             getFavs().collect {
                 internalFavsCache.value = it
                     .map { it.id to it }
                     .toMap()
             }

        }
    }

    fun getImages() = repository.observeNewsListPaginated(DEFAULT_PAGE_SIZE)

    fun getImage(imageId: String) = viewModelScope.launch {
        try {
            imageStateFlow.tryEmit(DetailStates.Success(repository.getImage(imageId)))
        }catch (e: Throwable){
            imageStateFlow.tryEmit(DetailStates.Error(e))
        }
    }

    fun getFavs() = repository.observeFavs(viewModelScope)

    fun isFavorite(imageRoot: ImageRoot): Boolean = internalFavsCache.value[imageRoot.id] != null

    fun addFavorite(imageRoot: ImageRoot) = viewModelScope.launch {
        repository.addFavorite(imageRoot)
    }

    fun removeFavorite(imageRoot: ImageRoot) = viewModelScope.launch {
        repository.removeFavorite(imageRoot)
    }

}