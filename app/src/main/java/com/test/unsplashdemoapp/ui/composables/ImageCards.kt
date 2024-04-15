package com.test.unsplashdemoapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.test.unsplashdemoapp.models.ImageRoot
import com.test.unsplashdemoapp.theme.shimmerHighLight
import com.test.unsplashdemoapp.utils.Fakes
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun ItemList(
    image: ImageRoot,
    isFavorite: Boolean,
    isFavCallback: (value: Boolean) -> Unit,
    onClick: () -> Unit
) {

        CardImage(
            image,
            isFavorite,
            isFavCallback,
            onClick
        )
}

@Composable
fun CardImage(
    imageRoot: ImageRoot,
    isFav: Boolean,
    isFavCallback: (value: Boolean) -> Unit,
    click: () -> Unit
){
    Card(
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .padding(8.dp)
            .clickable(onClick = click)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            CoilImage(
                imageModel = imageRoot.urls.thumbImage,
                contentDescription = "Image showed",
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.surface,
                    highlightColor = shimmerHighLight,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop,
            )
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth(0.86f)
                ) {
                    Text(
                        text = imageRoot.description ?: (imageRoot.altDescription ?: ""),
                        style = MaterialTheme.typography.subtitle2
                    )
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = rememberImagePainter(
                                data = imageRoot.user.profileImage.small,
                                builder = {
                                    transformations(CircleCropTransformation())
                                }
                            ),
                            contentDescription = "profile picture",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = imageRoot.user.name,
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FavoriteButton(
                        isChecked = isFav,
                        onClick = isFavCallback
                    )
                    Text(
                        imageRoot.likes.toString(),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview(){
    CardImage(Fakes.getDummyUser(), false, isFavCallback = { value ->  println(value)}) { println("Item clicked!") }
}