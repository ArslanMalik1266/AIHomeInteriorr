package org.yourappdev.homeinterior.ui.Explore


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.keyboard_arrow_down_24px
import homeinterior.composeapp.generated.resources.keyboard_arrow_up_24px
import org.jetbrains.compose.resources.painterResource

data class FilterState(
    var selectedRoomTypes: Set<String> = setOf("Bedroom"),
    var selectedStyles: Set<String> = setOf("Modern"),
    var selectedColors: Set<Int> = setOf(3),
    var selectedFormats: Set<String> = setOf("JPEG"),
    var selectedPrices: Set<String> = setOf("Free")
)

@Composable
fun FilterBottomSheetContent(
    onApplyFilters: (FilterState) -> Unit,
    onCancel: () -> Unit
) {
    var filterState by remember { mutableStateOf(FilterState()) }
    var expandedRoomType by remember { mutableStateOf(false) }
    var expandedStyle by remember { mutableStateOf(false) }
    var expandedColor by remember { mutableStateOf(false) }
    var expandedFormat by remember { mutableStateOf(false) }
    var expandedPrice by remember { mutableStateOf(false) }

    val primaryGreen = Color(0xFFA3B18A)
    val darkText = Color(0xFF2C2C2C)
    val mediumText = Color(0xFF323232)
    val lightText = Color(0xFF4D4D4D)
    val borderGray = Color(0xFF7D7A7A)
    val dividerGray = Color(0xFFBBBBBB)
    val cancelTextColor = Color(0xFF8C8989)
    val cancelBorderColor = Color(0xFFE1DDDD)
    val whiteText = Color(0xFFFEF7F7)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 21.dp),
    ) {

        stickyHeader {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Filters",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = darkText
                    )
                    Text(
                        text = " (2)",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryGreen
                    )
                }
                Text(
                    text = "Clear all",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = primaryGreen,
                    modifier = Modifier.clickable {
                        filterState = FilterState()
                    }
                )
            }
        }


        item {
            ExpandableFilterSection(
                title = "Type of Room",
                expanded = expandedRoomType,
                onExpandChange = { expandedRoomType = it },
                dividerColor = dividerGray,
                titleColor = mediumText
            ) {
                RoomTypeOptions(
                    selectedOptions = filterState.selectedRoomTypes,
                    onOptionsSelected = { filterState = filterState.copy(selectedRoomTypes = it) },
                    primaryGreen = primaryGreen,
                    borderGray = borderGray,
                    lightText = lightText
                )
            }
        }

        item {
            ExpandableFilterSection(
                title = "Style",
                expanded = expandedStyle,
                onExpandChange = { expandedStyle = it },
                dividerColor = dividerGray,
                titleColor = mediumText
            ) {
                StyleOptions(
                    selectedOptions = filterState.selectedStyles,
                    onOptionsSelected = { filterState = filterState.copy(selectedStyles = it) },
                    primaryGreen = primaryGreen,
                    borderGray = borderGray,
                    lightText = lightText
                )
            }
        }

        item {
            ExpandableFilterSection(
                title = "Colour",
                expanded = expandedColor,
                onExpandChange = { expandedColor = it },
                dividerColor = dividerGray,
                titleColor = mediumText
            ) {
                ColorOptions(
                    selectedColorIndices = filterState.selectedColors,
                    onColorSelected = { filterState = filterState.copy(selectedColors = it) },
                    primaryGreen = primaryGreen
                )
            }
        }

        item {
            ExpandableFilterSection(
                title = "By Format",
                expanded = expandedFormat,
                onExpandChange = { expandedFormat = it },
                dividerColor = dividerGray,
                titleColor = mediumText
            ) {
                FormatOptions(
                    selectedOptions = filterState.selectedFormats,
                    onOptionsSelected = { filterState = filterState.copy(selectedFormats = it) },
                    primaryGreen = primaryGreen,
                    borderGray = borderGray,
                    lightText = lightText
                )
            }
        }

        item {
            ExpandableFilterSection(
                title = "Price",
                expanded = expandedPrice,
                onExpandChange = { expandedPrice = it },
                dividerColor = dividerGray,
                titleColor = mediumText
            ) {
                PriceOptions(
                    selectedOptions = filterState.selectedPrices,
                    onOptionsSelected = { filterState = filterState.copy(selectedPrices = it) },
                    primaryGreen = primaryGreen,
                    borderGray = borderGray,
                    lightText = lightText
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onApplyFilters(filterState) },
                    modifier = Modifier.padding(end = 10.dp),
                    shape = RoundedCornerShape(7.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryGreen
                    ),
                ) {
                    Text(
                        text = "Apply Filters",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = whiteText,
                        letterSpacing = (-0.5).sp,
                        modifier = Modifier.padding(vertical = 3.dp, horizontal = 4.dp)
                    )
                }

                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(7.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, cancelBorderColor),
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = cancelTextColor,
                        letterSpacing = (-0.5).sp,
                        modifier = Modifier.padding(vertical = 3.dp, horizontal = 4.dp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ExpandableFilterSection(
    title: String,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    dividerColor: Color,
    titleColor: Color,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .clickable { onExpandChange(!expanded) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
            )

            Image(
                painter = painterResource(if (expanded) Res.drawable.keyboard_arrow_up_24px else Res.drawable.keyboard_arrow_down_24px),
                contentDescription = null,
                modifier = Modifier
            )
        }

        androidx.compose.animation.AnimatedVisibility(
            visible = expanded,
            enter = androidx.compose.animation.expandVertically(
                animationSpec = androidx.compose.animation.core.tween(
                    durationMillis = 300,
                    easing = androidx.compose.animation.core.FastOutSlowInEasing
                )
            ) + androidx.compose.animation.fadeIn(
                animationSpec = androidx.compose.animation.core.tween(
                    durationMillis = 300
                )
            ),
            exit = androidx.compose.animation.shrinkVertically(
                animationSpec = androidx.compose.animation.core.tween(
                    durationMillis = 300,
                    easing = androidx.compose.animation.core.FastOutSlowInEasing
                )
            ) + androidx.compose.animation.fadeOut(
                animationSpec = androidx.compose.animation.core.tween(
                    durationMillis = 300
                )
            )
        ) {
            Column {
                Spacer(modifier = Modifier.height(5.dp))
                content()
            }
        }
    }
}

@Composable
fun RadioOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    primaryGreen: Color,
    borderGray: Color,
    lightText: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .border(0.7.dp, borderGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(primaryGreen, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = lightText,
            )
        }
    }
}

@Composable
fun RoomTypeOptions(
    selectedOptions: Set<String>,
    onOptionsSelected: (Set<String>) -> Unit,
    primaryGreen: Color,
    borderGray: Color,
    lightText: Color
) {
    val allOptions = listOf(
        "All", "Bedroom", "Living Room", "TV Lounge", "Drawing Room", "Dining Room",
        "Loft", "Guest Bedroom", "Home Theater Room", "Pantry / Store Room",
        "Utility Room", "Kids' Bedroom", "Nursery", "Study Room", "Home Office",
        "Bathroom", "Powder Room", "Balcony", "Terrace", "Patio", "Prayer Room",
        "Kitchen", "Garage", "Home Gym", "Walk-in Closet", "Basement", "Foyer/Entrance Hall"
    )
    val optionsWithoutAll = allOptions.filter { it != "All" }


    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        maxItemsInEachRow = 2
    ) {
        allOptions.forEach { option ->
            Box(modifier = Modifier.weight(1f)) {
                RadioOption(
                    text = option,
                    selected = selectedOptions.contains(option),
                    onClick = {
                        val newSelection = when {
                            option == "All" -> {
                                if (selectedOptions.contains("All")) {
                                    val firstOptionAfterAll = allOptions.getOrNull(1)
                                    if (firstOptionAfterAll != null) {
                                        selectedOptions - "All" - firstOptionAfterAll
                                    } else {
                                        selectedOptions - "All"
                                    }
                                } else {
                                    allOptions.toSet()
                                }
                            }

                            selectedOptions.contains("All") && option != "All" -> {
                                (allOptions.toSet() - "All" - option).ifEmpty { emptySet() }
                            }

                            selectedOptions.contains(option) -> {
                                (selectedOptions - option).ifEmpty { emptySet() }
                            }

                            else -> {
                                val updated = selectedOptions + option
                                if (updated.size == optionsWithoutAll.size) updated + "All" else updated
                            }
                        }
                        onOptionsSelected(newSelection)
                    },
                    primaryGreen = primaryGreen,
                    borderGray = borderGray,
                    lightText = lightText
                )
            }
        }
    }
}

@Composable
fun StyleOptions(
    selectedOptions: Set<String>,
    onOptionsSelected: (Set<String>) -> Unit,
    primaryGreen: Color,
    borderGray: Color,
    lightText: Color
) {
    val allOptions = listOf(
        "All", "Modern", "Contemporary", "Minimalist", "Scandinavian", "Japanese",
        "Boho Chic", "Industrial", "Luxury", "Classic", "Mid-Century", "Urban Modern",
        "Rustic Modern", "Eco-Friendly", "Coastal"
    )
    val optionsWithoutAll = allOptions.filter { it != "All" }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        maxItemsInEachRow = 2
    ) {
        allOptions.forEach { option ->
            Box(modifier = Modifier.weight(1f)) {
                RadioOption(
                    text = option,
                    selected = selectedOptions.contains(option),
                    onClick = {
                        val newSelection = when {
                            option == "All" -> {
                                if (selectedOptions.contains("All")) {
                                    val firstOptionAfterAll = allOptions.getOrNull(1)
                                    if (firstOptionAfterAll != null) {
                                        selectedOptions - "All" - firstOptionAfterAll
                                    } else {
                                        selectedOptions - "All"
                                    }
                                } else {
                                    allOptions.toSet()
                                }
                            }

                            selectedOptions.contains("All") && option != "All" -> {
                                (allOptions.toSet() - "All" - option).ifEmpty { emptySet() }
                            }

                            selectedOptions.contains(option) -> {
                                (selectedOptions - option).ifEmpty { emptySet() }
                            }

                            else -> {
                                val updated = selectedOptions + option
                                if (updated.size == optionsWithoutAll.size) updated + "All" else updated
                            }
                        }
                        onOptionsSelected(newSelection)
                    },
                    primaryGreen = primaryGreen,
                    borderGray = borderGray,
                    lightText = lightText
                )
            }
        }
    }
}

@Composable
fun ColorOptions(
    selectedColorIndices: Set<Int>,
    onColorSelected: (Set<Int>) -> Unit,
    primaryGreen: Color
) {
    val colors = listOf(
        Color(0xFFD9D9D9), Color(0xFFD9D9D9), Color(0xFFD9D9D9), Color(0xFFD9D9D9),
        Color(0xFFD9D9D9), Color(0xFFD9D9D9), Color(0xFFD9D9D9), Color(0xFFD9D9D9),
        Color(0xFFD9D9D9), Color(0xFFD9D9D9), Color(0xFFD9D9D9), Color(0xFFD9D9D9),
        Color(0xFFD9D9D9), Color(0xFFD9D9D9), Color(0xFFD9D9D9), Color(0xFFD9D9D9),
        Color(0xFFD9D9D9), Color(0xFFD9D9D9), Color(0xFFD9D9D9), Color(0xFFD9D9D9)
    )

    LazyHorizontalStaggeredGrid(
        rows = StaggeredGridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)  // Adjust height based on your needs
            .padding(start = 10.dp, end = 10.dp, bottom = 18.dp),
        horizontalItemSpacing = 11.dp,
        verticalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        items(colors.size) { index ->
            val isSelected = selectedColorIndices.contains(index)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) Color(0xFFCBE0A7) else Color.Transparent,
                        shape = CircleShape
                    )
                    .background(colors[index], CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        val newSelection = if (isSelected) {
                            selectedColorIndices - index
                        } else {
                            selectedColorIndices + index
                        }
                        onColorSelected(newSelection)
                    }
            )
        }
    }
}


@Composable
fun FormatOptions(
    selectedOptions: Set<String>,
    onOptionsSelected: (Set<String>) -> Unit,
    primaryGreen: Color,
    borderGray: Color,
    lightText: Color
) {
    val allOptions = listOf("All", "JPEG", "PNG", "PDF")
    val optionsWithoutAll = allOptions.filter { it != "All" }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        maxItemsInEachRow = 2
    ) {
        allOptions.forEach { option ->
            Box(modifier = Modifier.weight(1f)) {
                RadioOption(
                    text = option,
                    selected = selectedOptions.contains(option),
                    onClick = {
                        val newSelection = when {
                            option == "All" -> {
                                if (selectedOptions.contains("All")) {
                                    val firstOptionAfterAll = allOptions.getOrNull(1)
                                    if (firstOptionAfterAll != null) {
                                        selectedOptions - "All" - firstOptionAfterAll
                                    } else {
                                        selectedOptions - "All"
                                    }
                                } else {
                                    allOptions.toSet()
                                }
                            }

                            selectedOptions.contains("All") && option != "All" -> {
                                (allOptions.toSet() - "All" - option).ifEmpty { emptySet() }
                            }

                            selectedOptions.contains(option) -> {
                                (selectedOptions - option).ifEmpty { emptySet() }
                            }

                            else -> {
                                val updated = selectedOptions + option
                                if (updated.size == optionsWithoutAll.size) updated + "All" else updated
                            }
                        }
                        onOptionsSelected(newSelection)
                    },
                    primaryGreen = primaryGreen,
                    borderGray = borderGray,
                    lightText = lightText
                )
            }
        }
    }
}

@Composable
fun PriceOptions(
    selectedOptions: Set<String>,
    onOptionsSelected: (Set<String>) -> Unit,
    primaryGreen: Color,
    borderGray: Color,
    lightText: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.weight(1f)) {
            RadioOption(
                text = "Free",
                selected = selectedOptions.contains("Free"),
                onClick = {
                    val newSelection = if (selectedOptions.contains("Free")) {
                        selectedOptions - "Free"
                    } else {
                        selectedOptions + "Free"
                    }
                    onOptionsSelected(newSelection)
                },
                primaryGreen = primaryGreen,
                borderGray = borderGray,
                lightText = lightText
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            RadioOption(
                text = "Premium",
                selected = selectedOptions.contains("Premium"),
                onClick = {
                    val newSelection = if (selectedOptions.contains("Premium")) {
                        selectedOptions - "Premium"
                    } else {
                        selectedOptions + "Premium"
                    }
                    onOptionsSelected(newSelection)
                },
                primaryGreen = primaryGreen,
                borderGray = borderGray,
                lightText = lightText
            )
        }
    }
}
