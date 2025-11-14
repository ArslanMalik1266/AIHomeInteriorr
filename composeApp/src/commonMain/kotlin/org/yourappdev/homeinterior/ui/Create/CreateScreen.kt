package org.yourappdev.homeinterior.ui.Create


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.add_2_24px
import homeinterior.composeapp.generated.resources.createpageimage
import homeinterior.composeapp.generated.resources.premiumicon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

data class RoomCategory(
    val name: String,
    val imageRes: Int? = null,
    val height: Int = 126
)

@Composable
fun CreateScreen(onClick: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Header() {
            onClick()
        }
        EmptyStateCard() {

        }
        Column(modifier = Modifier.verticalScroll(scrollState), verticalArrangement = Arrangement.spacedBy(32.dp)) {
            TrendingSection()
            RecentFilesSection()
        }
    }
}

@Composable
fun Header(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 24.dp,end = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "Interior AI",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C2C2C),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
        Box(modifier = Modifier.size(30.dp).clip(CircleShape).clickable {
            onClick()
        }, contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(Res.drawable.premiumicon),
                contentDescription = "",
                modifier = Modifier.size(21.dp)
            )
        }
    }
}

@Composable
private fun EmptyStateCard(onClick: () -> Unit) {
    Box(modifier = Modifier.padding(start = 24.dp,end = 24.dp)) {

        Image(painter = painterResource(Res.drawable.createpageimage), contentDescription = null)
        Surface(
            onClick = {
                onClick()
            },
            color = Color(0xFFAAB892),
            shape = RoundedCornerShape(20),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 15.dp)
                .height(28.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.add_2_24px),
                    contentDescription = "Add photo",
                    colorFilter = ColorFilter.tint(color = Color.White),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Add photo",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 12.sp,
                    letterSpacing = (-0.5).sp
                )
            }
        }
    }
}

@Composable
private fun TrendingSection() {
    Column {
        Text(
            text = "Trending",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(start = 24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TrendingGrid()
    }
}

@Composable
private fun TrendingGrid() {
    val rooms = listOf(
        RoomCategory("Bedroom"),
        RoomCategory("Guest Room"),
        RoomCategory("Bathroom"),
        RoomCategory("TV Console"),
        RoomCategory("Dinning Room"),
        RoomCategory("Terrace"),
        RoomCategory("Walk-in Closet"),
        RoomCategory("Living Room"),
        RoomCategory("Stair Wall"),
        RoomCategory("Kitchen Cabinet"),
        RoomCategory("Master Bathroom"),
        RoomCategory("Dinning Room")
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 10.dp),
        modifier = Modifier.height(260.dp)
    ) {
        items(rooms.chunked(2)) { columnItems ->
            val columnIndex = rooms.chunked(2).indexOf(columnItems)
            val isAlternate = columnIndex % 2 == 1

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                columnItems.forEachIndexed { index, room ->
                    val height = when {
                        isAlternate && index == 1 -> 95.dp
                        isAlternate && index == 0 -> 156.dp
                        else -> 126.dp
                    }

                    RoomCategoryCard(
                        room = room,
                        modifier = Modifier.height(height)
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
private fun RoomCategoryCard(room: RoomCategory, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(126.dp)
            .height(room.height.dp)
            .clip(RoundedCornerShape(8.782.dp))
            .background(Color(0xFFE8E8E8))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp) // Adjust as needed
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.3f to Color.Black.copy(alpha = 0.1f),
                        0.6f to Color.Black.copy(alpha = 0.4f),
                        1.0f to Color.Black.copy(alpha = 0.6f)
                    )
                )
        )
        Text(
            text = room.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        )
    }
}

@Composable
private fun RecentFilesSection() {
    Column(modifier = Modifier.padding(bottom = 30.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Files",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(start = 24.dp)
            )

            Text(
                text = "See all",
                fontSize = 13.sp,
                fontWeight = FontWeight.Light,
                color = Color(0xFF8D8D8D)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RecentFilesRow()
    }
}

@Composable
private fun RecentFilesRow() {
    val recentFiles = listOf(1, 2, 3, 4)

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 10.dp),
    ) {
        items(recentFiles) { index ->
            Box(
                modifier = Modifier
                    .size(114.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE8E8E8))
            )
        }
        item {
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}
