package com.test.unsplashdemoapp.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.test.unsplashdemoapp.ui.SharedViewModel
import com.test.unsplashdemoapp.ui.composables.ItemList

@Composable
fun HomeView(
    viewModel: SharedViewModel,
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {
        val lazyPages = viewModel.getImages().flow.collectAsLazyPagingItems()
        LazyColumn {
            items(lazyPages) { image ->
                image?.let {
                    val isFavorite by viewModel.favsCache.collectAsState()
                    ItemList(image, isFavorite.containsKey(image.id),
                        isFavCallback = { value ->
                            if (value) {
                                viewModel.addFavorite(image)
                            } else {
                                viewModel.removeFavorite(image)
                            }
                        }) {
                        navController.navigate("detail/${image.id}")
                    }
                }
            }
        }
    }
}