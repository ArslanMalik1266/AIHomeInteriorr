package org.yourappdev.homeinterior.ui.Generate


import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.arrow_back_
import homeinterior.composeapp.generated.resources.close
import org.jetbrains.compose.resources.painterResource

data class ColorPalette(
    val colors: List<Color>,
    val id: Int
)

@Composable
fun ColorPaletteSelectionScreen() {
    var selectedPaletteId by remember { mutableStateOf(0) }
    val listState = rememberLazyListState()
    val colorPalettes = listOf(
        ColorPalette(
            colors = listOf(
                Color(0xFF906C1F),
                Color(0xFFC69735),
                Color(0xFFBFA564),
                Color(0xFFDFC077),
                Color(0xFFEBDAB0)
            ),
            id = 0
        ),
        ColorPalette(
            colors = listOf(
                Color(0xFF485F4C),
                Color(0xFF778E7B),
                Color(0xFF9CB6A1),
                Color(0xFFBDDEC3),
                Color(0xFFCBE4D0)
            ),
            id = 1
        ),
        ColorPalette(
            colors = listOf(
                Color(0xFFD53D52),
                Color(0xFFDE596B),
                Color(0xFFDB6E7D),
                Color(0xFFDB98A1),
                Color(0xFFEBC7CC)
            ),
            id = 2
        ),
        ColorPalette(
            colors = listOf(
                Color(0xFF878787),
                Color(0xFFA7A7A7),
                Color(0xFFB7B6B6),
                Color(0xFFCECCCC),
                Color(0xFFE6E3E3)
            ),
            id = 3
        ),
        ColorPalette(
            colors = listOf(
                Color(0xFF8E50DF),
                Color(0xFFA173DD),
                Color(0xFFBBA0DD),
                Color(0xFFE1C9FF),
                Color(0xFFEEE4FB)
            ),
            id = 4
        ),
        ColorPalette(
            colors = listOf(
                Color(0xFFFAFFD2),
                Color(0xFFFCFFE2),
                Color(0xFFFCFFE5),
                Color(0xFFFCFDF2),
                Color(0xFFFFFFFF)
            ),
            id = 5
        ),
        ColorPalette(
            colors = listOf(
                Color(0xFF0099FF),
                Color(0xFF4AB0F3),
                Color(0xFF74C5FC),
                Color(0xFF9CD7FF),
                Color(0xFFC0DCEE)
            ),
            id = 6
        ),
        ColorPalette(
            colors = listOf(
                Color(0xFFF8E728),
                Color(0xFFFFF47C),
                Color(0xFFFFF7A1),
                Color(0xFFF9F5C3),
                Color(0xFFF9F6D3)
            ),
            id = 7
        )
    )
    val isLastItemVisible by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index == colorPalettes.size - 1
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

            HeaderWithSearch("Color Pallete")

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(colorPalettes.size) { index ->
                        ColorPaletteRow(
                            palette = colorPalettes[index],
                            isSelected = colorPalettes[index].id == selectedPaletteId,
                            onClick = { selectedPaletteId = colorPalettes[index].id }
                        )
                    }

                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = !isLastItemVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
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

    }
}


@Composable
private fun ColorPaletteRow(
    palette: ColorPalette,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(9.dp))
            .background(Color.White)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFFCBE0A7) else Color(0xFFE7E7E7),
                shape = RoundedCornerShape(9.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 38.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            palette.colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(
                            elevation = 14.dp,
                            shape = CircleShape,
                            spotColor = Color.Black.copy(alpha = 0.13f)
                        )
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}
