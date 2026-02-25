package org.yourappdev.homeinterior.ui.Generate.UiScreens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.edit_icon
import homeinterior.composeapp.generated.resources.generate
import homeinterior.composeapp.generated.resources.sofa
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.yourappdev.homeinterior.domain.model.AboutToGenerateUiState
import org.yourappdev.homeinterior.ui.CreateAndExplore.RoomsViewModel
import org.yourappdev.homeinterior.ui.UiUtils.CloseIconButton
import org.yourappdev.homeinterior.utils.Logger



@Composable
fun AboutToGenerateScreen(roomsViewModel: RoomsViewModel = koinViewModel(),
                          onCloseClick: () -> Unit,
                          onResult: () -> Unit ) {
    val state by roomsViewModel.state.collectAsState()
    LaunchedEffect(state.selectedImage) {
        println("DEBUG_SCREEN: SelectedImage from ViewModel = ${state.selectedImage}")
    }

    val selectedType = state.selectedRoomType ?: ""
    val selectedStyle = state.selectedStyleName ?: ""
    val selectedImage =state.selectedImage

    LaunchedEffect(state.selectedImage) {
        println("DEBUGG = ${state.selectedImage}")
    }


    val backgroundColor = Color(0xFFFFFFFF)
    val borderColor = Color(0xFFD7D6D6)
    val selectedBorderColor = Color(0xFFACBE8D)


    LaunchedEffect(state) {
        println("AboutToGenerateDebug: $state")
    }

    var showLoader by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(showLoader) {
        if (showLoader) {
            delay(5000)
            showLoader = false
            onResult()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            TopBar() {
                onCloseClick()
            }

            Spacer(modifier = Modifier.height(10.dp))

            ImagePreview(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                imageUri = selectedImage
            )

            Spacer(modifier = Modifier.height(20.dp))

            SelectionCard(
                label = "Type",
                value = selectedType,
                borderColor = selectedBorderColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)

            )

            Spacer(modifier = Modifier.height(8.dp))

            SelectionCard(
                label = "Style",
                value = selectedStyle.toString(),
                borderColor = borderColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ColorPaletteCard(
                borderColor = borderColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            GenerateButton(
                modifier = Modifier
                    .width(170.dp)
                    .height(49.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                showLoader = true
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
        if (showLoader) {
            LoadingScreen()
        }
    }
}

@Composable
fun TopBar(onCloseClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        CloseIconButton(tint = Color(0xFFB2B0B0)) {
            onCloseClick()
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Create",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C2C2C)
        )
    }
}

@Composable
private fun ImagePreview(
    modifier: Modifier = Modifier,
    imageUri: Any?
) {
    Box(
        modifier = modifier
            .fillMaxHeight(0.45f)
            .clip(RoundedCornerShape(9.dp))
            .background(Color(0xFFF5F5F5))
    ) {

        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Room Preview",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = "No Image Selected",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun SelectionCard(
    label: String,
    value: String,
    borderColor: Color,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = Color(0xFFFFFFFF).copy(alpha = 0.57f)
    val mediumText = Color(0xFF4D4D4D)
    val grayText = Color(0xFF91918F)
    val editIconColor = Color(0xFFB3B5B1)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(9.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(9.dp)
            )
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = mediumText,
                lineHeight = 20.sp
            )
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = grayText,
                lineHeight = 20.sp
            )
        }

        Icon(
            painter = painterResource(Res.drawable.edit_icon),
            contentDescription = "Edit",
            tint = editIconColor,
            modifier = Modifier
                .size(22.dp)
                .align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun ColorPaletteCard(
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(0xFFFFFFFF).copy(alpha = 0.57f)
    val lightGrayText = Color(0xFF90918F)
    val editIconColor = Color(0xFFB3B5B1)

    val paletteColors = listOf(
        Color(0xFF485F4C),
        Color(0xFFC69735),
        Color(0xFFD3CCBB)
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(9.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(9.dp)
            )
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy((-9).dp),
            ) {
                paletteColors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .shadow(
                                elevation = 5.dp,
                                shape = CircleShape,
                                ambientColor = Color.Black.copy(alpha = 0.2f),
                                spotColor = Color.Black.copy(alpha = 0.2f)
                            )
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 0.2.dp,
                                color = Color(0xFF898989),
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Colour Pallete",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = lightGrayText
            )
        }

        Icon(
            painter = painterResource(Res.drawable.edit_icon),
            contentDescription = "Edit",
            tint = editIconColor,
            modifier = Modifier
                .size(22.dp)
                .align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun GenerateButton(modifier: Modifier = Modifier, onGenerateClick: () -> Unit) {
    val buttonGradient = Brush.linearGradient(
        0.0f to Color(0xFFFFFFFF),
        0.37f to Color(0xFFFFFFFF),
        1.0f to Color(0xFFCEFFB3).copy(alpha = 0.48f)
    )



    Button(
        onClick = {
            onGenerateClick()
        },
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp),
        border = BorderStroke(
            width = 1.5.dp,
            color = Color(0xFFD2FDB9)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = buttonGradient,
                    shape = RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Box(
                    modifier = Modifier.padding(end = 9.dp)
                        .size(40.2.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            ambientColor = Color.Black.copy(alpha = 0.07f),
                            spotColor = Color.Black.copy(alpha = 0.07f)
                        )
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFC5EBB2).copy(alpha = 0.69f),
                                    Color(0xFFDFF2C2).copy(alpha = 0.69f),
                                    Color(0xFFD2F7BD).copy(alpha = 0.69f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.generate),
                        contentDescription = "Generate Icon",
                        tint = Color(0xFFFFD13A),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Generate",
                    fontSize = 19.67.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }
    }


}


