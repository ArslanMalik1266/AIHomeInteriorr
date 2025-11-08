package org.yourappdev.homeinterior.ui.Generate


import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.arrow_back_
import homeinterior.composeapp.generated.resources.close
import homeinterior.composeapp.generated.resources.sofa
import homeinterior.composeapp.generated.resources.sofa_2
import homeinterior.composeapp.generated.resources.sofa_3
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class InteriorStyle(
    val name: String,
    val imageRes: DrawableResource,
    val id: Int
)

@Composable
fun StyleSelectionScreen() {
    var selectedStyleId by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }

    val interiorStyles = listOf(
        InteriorStyle("Modern", Res.drawable.sofa, 0),
        InteriorStyle("Contemporary", Res.drawable.sofa_2, 1),
        InteriorStyle("Minimalist", Res.drawable.sofa_3, 2),
        InteriorStyle("Scandinavian", Res.drawable.sofa, 3),
        InteriorStyle("Japanese", Res.drawable.sofa_2, 4),
        InteriorStyle("Boho Chic", Res.drawable.sofa_3, 5),
        InteriorStyle("Industrial", Res.drawable.sofa, 6),
        InteriorStyle("Luxury", Res.drawable.sofa_2, 7),
        InteriorStyle("Classic", Res.drawable.sofa_3, 8),
        InteriorStyle("Mid-Century", Res.drawable.sofa, 9),
        InteriorStyle("Urban Modern", Res.drawable.sofa_2, 10),
        InteriorStyle("Rustic Modern", Res.drawable.sofa_3, 11),
    )
    val listState = rememberLazyGridState()
    val isLastItemVisible by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index == interiorStyles.size - 1
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {


            HeaderWithSearch("Style")

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(interiorStyles) { style ->
                    StyleCard(
                        style = style,
                        isSelected = style.id == selectedStyleId,
                        onClick = { selectedStyleId = style.id },
                    )
                }
            }
        }
        androidx.compose.animation.AnimatedVisibility(
            visible = !isLastItemVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x00FFFFFF),
                                Color(0xF5FFFFFF)
                            ),
                            startY = 0f,
                            endY = 900f
                        )
                    )
            )
        }
    }
}


@Composable
private fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onClearSearch: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(44.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFF8F8F8).copy(alpha = 0.72f))
            .border(
                width = 0.5.dp,
                color = Color(0xFFE3E3E3),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (searchText.isEmpty()) {
                Text(
                    text = "Search here...",
                    fontSize = 14.sp,
                    color = Color(0xFFAFABAB),
                    fontWeight = FontWeight.Normal
                )
            } else {
                Text(
                    text = searchText,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )
            }

            Image(
                painter = painterResource(Res.drawable.close),
                contentDescription = "Clear",
                colorFilter = ColorFilter.tint(color = Color(0xFFB2B0B0)),
                modifier = Modifier
                    .size(11.dp)
                    .clickable { onClearSearch() }
            )
        }
    }
}

@Composable
private fun StyleCard(
    style: InteriorStyle,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(11.dp))
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) Color(0xFFCBE0A7) else Color.Transparent,
                shape = RoundedCornerShape(11.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(style.imageRes),
            contentDescription = style.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.7f to Color.Transparent,
                        1.0f to Color.Black.copy(alpha = 0.8f)
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = style.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
