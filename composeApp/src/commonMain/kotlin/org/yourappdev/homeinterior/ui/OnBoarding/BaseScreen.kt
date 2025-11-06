package org.yourappdev.homeinterior.ui.OnBoarding

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.page_
import homeinterior.composeapp.generated.resources.page_2
import homeinterior.composeapp.generated.resources.page_3
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.yourappdev.homeinterior.domain.model.BoardingData
import org.yourappdev.homeinterior.ui.UiUtils.ProgressIndicator
import org.yourappdev.homeinterior.ui.UiUtils.TopUShape

@Composable
fun BaseScreen(onEndReached: () -> Unit) {
    val scope = rememberCoroutineScope()
    val myList = remember {
        listOf(
            BoardingData(
                imageUri = Res.drawable.page_,
                title = "Find Inspiration for Every Space",
                subTitle = "Get inspired by modern spaces, timeless aesthetics, and intelligent design trends crafted to elevate your living experience."
            ),
            BoardingData(
                imageUri = Res.drawable.page_2,
                title = "Design. Transform. Compare. Effortlessly.",
                subTitle = "Explore your space’s full potential with intelligent design previews that bring every detail to life."
            ),
            BoardingData(
                imageUri = Res.drawable.page_3,
                title = "Transform Your Space with AI",
                subTitle = "Explore sleek, minimalist interiors, sophisticated color palettes, and functional designs tailored to your unique style."
            )
        )
    }
    val state = rememberPagerState(pageCount = { myList.size })
    Box(Modifier.fillMaxSize().background(Color(0xFFEDF8E7)), contentAlignment = Alignment.Center) {
        HorizontalPager(
            state = state
        ) {
            when (state.currentPage) {
                0 -> {
                    BoardingPage(myList[state.currentPage].imageUri)
                }

                1 -> {
                    BoardingPage(myList[state.currentPage].imageUri)
                }

                2 -> {
                    BoardingPage(myList[state.currentPage].imageUri)
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxHeight(0.5f).align(Alignment.BottomCenter)
                .clip(TopUShape())
                .background(Color.White),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(start = 20.dp, end = 20.dp, top = 30.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
                    Text(
                        text = myList[state.currentPage].title,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 25.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )


                    Text(
                        text = myList[state.currentPage].subTitle,
                        fontSize = 14.5.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        lineHeight = 21.sp,
                        color = Color(0xff979797)
                    )
                }

                ProgressIndicator(
                    currentStep = state.currentPage,
                    totalSteps = myList.size,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 17.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Skip",
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        onClick = {
                            if (state.currentPage < myList.size) {
                                scope.launch {
                                    state.animateScrollToPage(
                                        state.currentPage + 1,
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            easing = FastOutSlowInEasing
                                        )
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Continue", color = Color.White)
                    }
                }
            }
        }
    }
}
