package com.test.unsplashdemoapp.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.test.unsplashdemoapp.theme.shimmerHighLight
import com.test.unsplashdemoapp.ui.SharedViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun DetailView(
    upPress: () -> Unit,
    imageId: String,
    detailScreenViewModel: SharedViewModel
) {

    detailScreenViewModel.getImage(imageId)


    val image = detailScreenViewModel.imageStateFlow.collectAsState()
    val isFavorite by detailScreenViewModel.favsCache.collectAsState()

    when(val state = image.value){
        is DetailStates.Error -> Text(text = "Error ${state.e}")
        is DetailStates.Loading -> Text(
            text = "Content loaded",
            modifier = Modifier
                .padding(16.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(shimmerHighLight),
                    color = Color.White
                )
        )
        is DetailStates.Success -> SuccessDetailView(upPress, state.imageRoot, isFavorite.containsKey(state.imageRoot.id)){ value ->
            if (value) {
                detailScreenViewModel.addFavorite(state.imageRoot)
            } else {
                detailScreenViewModel.removeFavorite(state.imageRoot)
            }
        }
    }


}


