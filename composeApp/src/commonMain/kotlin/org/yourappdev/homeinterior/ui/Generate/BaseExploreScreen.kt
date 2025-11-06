package org.yourappdev.homeinterior.ui.Generate

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.arrow_back_
import homeinterior.composeapp.generated.resources.close
import homeinterior.composeapp.generated.resources.sofa
import org.jetbrains.compose.resources.painterResource
import org.yourappdev.homeinterior.ui.Files.ProBadge

@Composable
fun BaseAddScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White).statusBarsPadding(),
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().offset(y = (-10).dp), contentAlignment = Alignment.Center) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp),
                    shape = RoundedCornerShape(39.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFCFCFC),
                        contentColor = Color(0xFF828282)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0DADA))
                ) {
                    Text(
                        text = "Next",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) {
        val gradientColors = listOf(
            Color(0xFFC5EBB2),
            Color(0xFFDFF2C2),
            Color(0xFFC1DFB5),
            Color(0xFFD2F7BD)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            TopNavigationBar(gradientColors)

            Spacer(modifier = Modifier.height(24.dp))

            ProgressIndicator()

            RoomTypeSelection()
//            FirstPage()

//            HorizontalPager()
        }
    }
}


@Composable
private fun TopNavigationBar(gradientColors: List<Color>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProBadge(gradientColors)
        Text(
            text = "Step 2/4",
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF9C9C9C),
            modifier = Modifier.padding(end = 20.dp)
        )
        Image(
            painter = painterResource(Res.drawable.close),
            contentDescription = "back",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color(0xFFB2B0B0))
        )
    }
}

@Composable
private fun ProgressIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 118.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(4) { index ->
            Box(
                modifier = Modifier
                    .width(45.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (index == 0) Color(0xFFA3B18A) else Color(0xFFE4E2E2)
                    )
            )
        }
    }
}