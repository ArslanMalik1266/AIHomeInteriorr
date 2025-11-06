package org.yourappdev.homeinterior.ui.Explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
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
import homeinterior.composeapp.generated.resources.search
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import homeinterior.composeapp.generated.resources.filter
import homeinterior.composeapp.generated.resources.sofa
import homeinterior.composeapp.generated.resources.sofa_2
import homeinterior.composeapp.generated.resources.sofa_3
import org.jetbrains.compose.resources.DrawableResource
import org.yourappdev.homeinterior.ui.theme.chipColor
import org.yourappdev.homeinterior.ui.theme.fieldBack
import org.yourappdev.homeinterior.ui.theme.selectedNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(onFilterClick: () -> Unit) {
    var filterCount by remember { mutableStateOf(0) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val roomTypes = listOf("Bedroom", "Dining Room", "Bathroom", "Kitchen", "Lounge")
    val styles = listOf("Modern", "Minimal", "Classic", "Vintage", "Modern", "Lux")
    val colors = listOf(
        Color(0xFF8D6E63), Color(0xFFFFEB3B), Color(0xFF4CAF50), Color(0xFFE91E63), Color(0xFFFF9800), Color(0xFF795548)
    )
    val text = remember { mutableStateOf("") }

    val images = remember {
        listOf(
            Res.drawable.sofa,
            Res.drawable.sofa_2,
            Res.drawable.sofa_3,
            Res.drawable.sofa,
            Res.drawable.sofa_2,
            Res.drawable.sofa_3
        )
    }
    var showSheet by remember {
        mutableStateOf(false)
    }
    val sampleColors = listOf(
        Color(0xFFD2B48C), // Beige
        Color.Black,
        Color(0xFF8B4513) // Brown
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            stickyHeader {
                Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                    Text(
                        text = "Explore",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(end = 24.dp, bottom = 4.dp)
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
                                placeholder = { Text("Search") },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(Res.drawable.search),
                                        contentDescription = null
                                    )
                                },
                                singleLine = true,
                                enabled = true,
                                interactionSource = remember { MutableInteractionSource() },
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent
                                ),
                                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                                visualTransformation = VisualTransformation.None
                            )
                        }

                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .aspectRatio(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(fieldBack, RoundedCornerShape(10.dp))
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(enabled = true, onClick = {
                                        showSheet = true
                                    }),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(painter = painterResource(Res.drawable.filter), contentDescription = null)
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
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                Text(
                    text = "Type of Room",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 3.dp),
                    letterSpacing = 0.sp
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(roomTypes) { room ->
                        FilterChip(
                            selected = false,
                            onClick = onFilterClick,
                            label = { Text(room, fontSize = 15.sp, fontWeight = FontWeight.Medium) },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = chipColor,
                                selectedContainerColor = selectedNavItem,
                                selectedLabelColor = Color.White,
                                labelColor = Color.Black
                            ),
                            border = null
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }

            item {
                SpecificGridRow(images)
            }

            item {
                Text(
                    text = "Style",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 3.dp),
                    letterSpacing = 0.sp
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(styles) { style ->
                        FilterChip(
                            selected = false,
                            onClick = onFilterClick,
                            label = { Text(style, fontSize = 15.sp, fontWeight = FontWeight.Medium) },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = chipColor,
                                selectedContainerColor = selectedNavItem,
                                selectedLabelColor = Color.White,
                                labelColor = Color.Black
                            ),
                            border = null
                        )
                    }
                }
            }

            item {
                SpecificGridRow(images)
            }

            item {
                Text(
                    text = "Colour",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                StaggeredImageGallery(images = images, sampleColors)
            }
            item {
                Spacer(modifier = Modifier.height(33.dp))
            }
        }
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
                containerColor = Color.Transparent,
                dragHandle = null, modifier = Modifier.statusBarsPadding()
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
                        if (filters.selectedColor != null) {
                            filterCount++
                        }
                        if (filters.selectedFormats.isNotEmpty() && !filters.selectedFormats.contains("All")) {
                            filterCount++
                        }
                        if (filters.selectedPrice != "Free") {
                            filterCount++
                        }

                        showSheet = false
                    },
                    onCancel = {
                        showSheet = false
                    }
                )
            }
        }
    }
}


@Composable
fun StaggeredImageGallery(images: List<DrawableResource>, colors: List<Color>) {
    LazyHorizontalStaggeredGrid(
        rows = StaggeredGridCells.Fixed(2),
        horizontalItemSpacing = 8.dp,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(200.dp)
    ) {
        items(images) { imageRes ->
            ImageCard(
                imageRes = imageRes,
                height = 100.dp,
                colors = colors
            )
        }
        item {
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun ImageCard(imageRes: DrawableResource, height: Dp, colors: List<Color>) {
    Box(
        modifier = Modifier.height(height).clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Fit
        )
        OverlappingColorRow(
            Modifier.align(Alignment.BottomStart).padding(bottom = 5.dp, start = 5.dp),
            colors
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
            )
        }
    }
}


@Composable
fun SpecificGridRow(images: List<DrawableResource>) {
    LazyRow(
        modifier = Modifier.padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images.chunked(3)) { group ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(IntrinsicSize.Min),
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    group.take(2).forEach { image ->
                        Image(
                            painter = painterResource(image),
                            contentDescription = null,
                            modifier = Modifier
                                .width(120.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                if (group.size == 3) {
                    Image(
                        painter = painterResource(group[2]),
                        contentDescription = null,
                        modifier = Modifier
                            .width(165.dp)
                            .height(248.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
@Preview
fun showme() {
    ExploreScreen { }
}