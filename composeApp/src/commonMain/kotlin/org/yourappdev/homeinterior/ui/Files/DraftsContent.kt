package org.yourappdev.homeinterior.ui.Files

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.sofa
import homeinterior.composeapp.generated.resources.sofa_2
import homeinterior.composeapp.generated.resources.sofa_3
import org.jetbrains.compose.resources.painterResource

@Composable
fun DraftsContent(onImageClick: () -> Unit = {}) {
    val imageUrls = remember {
        listOf(
            Res.drawable.sofa_2,
            Res.drawable.sofa_3,
            Res.drawable.sofa,
            Res.drawable.sofa_2,
            Res.drawable.sofa_3,
            Res.drawable.sofa,
            Res.drawable.sofa_2,
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(imageUrls.size) { index ->
            Image(
                painter = painterResource(imageUrls[index]),
                contentDescription = "Interior design $index",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
                    .clip(RoundedCornerShape(11.dp)).clickable(enabled = true, onClick = onImageClick),
                contentScale = ContentScale.Crop
            )

//            AsyncImage(
//                model = imageUrls[index],
//                contentDescription = "Interior design $index",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(175.dp)
//                    .clip(RoundedCornerShape(11.dp)),
//                contentScale = ContentScale.Crop
//            )
        }
    }
}