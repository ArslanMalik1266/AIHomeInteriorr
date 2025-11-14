package org.yourappdev.homeinterior.ui.Explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.filter
import homeinterior.composeapp.generated.resources.sofa
import homeinterior.composeapp.generated.resources.sofa_2
import homeinterior.composeapp.generated.resources.sofa_3
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.VisualTransformation
import org.yourappdev.homeinterior.ui.theme.fieldBack
import kotlin.random.Random

data class ImageItem(
    val imageRes: DrawableResource,
    val height: Int, // Dynamic height multiplier
    val colors: List<Color>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(onFilterClick: () -> Unit) {
    var filterCount by remember { mutableStateOf(0) }
    val text = remember { mutableStateOf("") }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Generate dynamic image items with random heights and colors
    val imageItems = remember {
        val baseImages = listOf(
            Res.drawable.sofa,
            Res.drawable.sofa_2,
            Res.drawable.sofa_3
        )

        val colorPalettes = listOf(
            listOf(Color(0xFFD2B48C), Color.Black, Color(0xFF8B4513)),
            listOf(Color(0xFF8B6914), Color(0xFFC4A747), Color(0xFFD4B95A)),
            listOf(Color(0xFF2C5F2D), Color(0xFF4A7C59), Color(0xFF6B9B6E)),
            listOf(Color(0xFF4A5568), Color(0xFF718096), Color(0xFF8B95A5)),
            listOf(Color(0xFF6B46C1), Color(0xFF9F7AEA), Color(0xFFB794F6)),
            listOf(Color(0xFFE91E63), Color(0xFFF48FB1), Color(0xFFF8BBD0))
        )

        List(30) { index ->
            ImageItem(
                imageRes = baseImages[index % baseImages.size],
                height = Random.nextInt(150, 280),
                colors = colorPalettes[index % colorPalettes.size]
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Sticky Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "Explore",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 24.dp, bottom = 16.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                ) {
                    BasicTextField(
                        value = text.value,
                        onValueChange = { text.value = it },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.16.sp,
                            color = Color.Black
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f)
                            .background(fieldBack, RoundedCornerShape(10.dp))
                    ) { innerTextField ->
                        TextFieldDefaults.DecorationBox(
                            value = text.value,
                            innerTextField = innerTextField,
                            placeholder = { Text("Search here...") },
                            singleLine = true,
                            enabled = true,
                            interactionSource = remember { MutableInteractionSource() },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                            visualTransformation = VisualTransformation.None
                        )
                    }

                    Box(modifier = Modifier.height(50.dp).aspectRatio(1f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(fieldBack, RoundedCornerShape(10.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .clickable(enabled = true, onClick = { showSheet = true }),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.filter),
                                contentDescription = null
                            )
                        }

                        if (filterCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = 4.dp, y = (-4).dp)
                                    .background(Color(0xFFA3B18A), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = filterCount.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    lineHeight = 10.sp,
                                )
                            }
                        }
                    }
                }
            }

            // Vertical Staggered Grid
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalItemSpacing = 12.dp
            ) {
                items(imageItems) { item ->
                    ImageCard(
                        imageRes = item.imageRes,
                        height = item.height.dp,
                        colors = item.colors
                    )
                }
            }
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
                containerColor = Color.Transparent,
                dragHandle = null,
                modifier = Modifier.statusBarsPadding()
            ) {
                FilterBottomSheetContent(
                    onApplyFilters = { filters ->
                        filterCount = 0
                        if (filters.selectedRoomTypes.isNotEmpty() && !filters.selectedRoomTypes.contains("All")) {
                            filterCount++
                        }
                        if (filters.selectedStyles.isNotEmpty() && !filters.selectedStyles.contains("All")) {
                            filterCount++
                        }
                        if (filters.selectedColors.isNotEmpty() && !filters.selectedStyles.contains("All")) {
                            filterCount++
                        }
                        if (filters.selectedFormats.isNotEmpty() && !filters.selectedFormats.contains("All")) {
                            filterCount++
                        }
                        if (filters.selectedPrices.isNotEmpty()) {
                            filterCount++
                        }
                        showSheet = false
                    },
                    onCancel = { showSheet = false }
                )
            }
        }
    }
}

@Composable
fun ImageCard(imageRes: DrawableResource, height: androidx.compose.ui.unit.Dp, colors: List<Color>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        OverlappingColorRow(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 8.dp, start = 8.dp),
            colors = colors
        )
    }
}

@Composable
fun OverlappingColorRow(modifier: Modifier = Modifier, colors: List<Color>) {
    Box(
        modifier = modifier
            .size(
                width = (20.dp * colors.size) - (7.5.dp * (colors.size - 1)),
                height = 20.dp
            )
    ) {
        colors.forEachIndexed { index, color ->
            Box(
                modifier = Modifier
                    .offset(x = (index * 12.5).dp)
                    .size(20.dp)
                    .background(color, shape = CircleShape)
                    .border(1.dp, Color.White, CircleShape)
            )
        }
    }
}