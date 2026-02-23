package org.yourappdev.homeinterior.ui.authentication.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.emailicon
import homeinterior.composeapp.generated.resources.google
import homeinterior.composeapp.generated.resources.hide_
import homeinterior.composeapp.generated.resources.passicon
import homeinterior.composeapp.generated.resources.show_1_
import kotlinx.coroutines.flow.SharedFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.yourappdev.homeinterior.data.remote.util.ResultState
import org.yourappdev.homeinterior.navigation.Routes
import org.yourappdev.homeinterior.ui.authentication.AuthViewModel
import org.yourappdev.homeinterior.ui.authentication.register.RegisterEvent
import org.yourappdev.homeinterior.ui.authentication.register.RegisterState
import org.yourappdev.homeinterior.ui.UiUtils.BackIconButton
import org.yourappdev.homeinterior.ui.UiUtils.ButtonWithIcon
import org.yourappdev.homeinterior.ui.UiUtils.ClickableText
import org.yourappdev.homeinterior.ui.UiUtils.CustomSnackbar
import org.yourappdev.homeinterior.ui.UiUtils.ProgressLoading
import org.yourappdev.homeinterior.ui.UiUtils.rememberCustomSnackbarState
import org.yourappdev.homeinterior.ui.common.base.CommonUiEvent
import org.yourappdev.homeinterior.ui.theme.buttonBack
import org.yourappdev.homeinterior.ui.theme.smallText

@Preview(showBackground = true)
@Composable
fun LoginRoot(authViewModel: AuthViewModel = koinViewModel(), navController: NavHostController) {
    val state by authViewModel.state.collectAsState()
    LoginScreen(navController, authViewModel.uiEvent, state, authViewModel::onRegisterFormEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    uiEvent: SharedFlow<CommonUiEvent>,
    state: RegisterState,
    onLoginEvent: (event: RegisterEvent) -> Unit
) {
    val snackBarState = rememberCustomSnackbarState()
    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is CommonUiEvent.ShowError -> {
                    snackBarState.showError(event.message)
                }

                CommonUiEvent.NavigateToSuccess -> {
                    navController.navigate(Routes.BaseAppScreen) {
                        popUpTo(navController.graph.startDestinationId)
                    }
                }

                is CommonUiEvent.ShowSuccess -> {
                    snackBarState.showSuccess(event.message)
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        if (state.loginResponse is ResultState.Loading) {
            ProgressLoading()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            BackIconButton {
                navController.navigateUp()
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Login",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = buttonBack
            )

            Text(
                text = "Login now to track all your expenses and income at a place!",
                fontSize = 14.sp,
                color = smallText,
                modifier = Modifier.padding(top = 8.dp),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email field
            Text(
                text = "Email",
                fontSize = 14.sp,
                color = smallText,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { onLoginEvent(RegisterEvent.EmailUpdate(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "Ex. abc@example.com",
                        color = Color(0xFFCCCCCC)
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.emailicon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = LocalContentColor.current)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = buttonBack,
                    focusedLeadingIconColor = buttonBack,
                    unfocusedLeadingIconColor = Color(0xffDBDBDB)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password field
            Text(
                text = "Your Password",
                fontSize = 14.sp,
                color = smallText,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = { onLoginEvent(RegisterEvent.PasswordUpdate(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "••••••••••",
                        color = Color(0xFFCCCCCC)
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.passicon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = LocalContentColor.current)
                    )
                },
                trailingIcon = {
                    Box(modifier = Modifier.size(30.dp).clip(CircleShape).clickable {
                        onLoginEvent(RegisterEvent.TogglePassword(!state.showPassword))
                    }, contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(if (state.showPassword) Res.drawable.show_1_ else Res.drawable.hide_),
                            contentDescription = "Close",
                            colorFilter = ColorFilter.tint(color = buttonBack),
                            modifier = Modifier.size(23.dp)
                        )
                    }
                },
                visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = buttonBack,
                    focusedLeadingIconColor = buttonBack,
                    unfocusedLeadingIconColor = Color(0xffDBDBDB)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.rememberMe,
                        onCheckedChange = { },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF9CA986),
                            uncheckedColor = Color(0xFFCCCCCC)
                        )
                    )
                    Text(
                        text = "Remember me",
                        fontSize = 12.sp,
                        color = smallText,
                        letterSpacing = (-0.12).sp,
                        lineHeight = 1.sp,
                    )
                }

                ClickableText(title = "Forget Password?", textSize = 12.sp, color = smallText) {
                    navController.navigate(Routes.ForgetEmail)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = { 
                    onLoginEvent(RegisterEvent.Login)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBack
                )
            ) {
                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xffD2CECE),
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp, horizontal = 10.dp)
            )

            ButtonWithIcon(image = Res.drawable.google, borderColor = buttonBack, title = "Continue with Google", onClick = {})

            Spacer(modifier = Modifier.height(24.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 14.sp,
                    color = smallText
                )

                ClickableText(
                    title = "Register",
                    textSize = 14.sp,
                    color = buttonBack,
                    fontWeight = FontWeight.Bold
                ) {
                    navController.navigate(Routes.Register)
                }
            }
        }
        CustomSnackbar(
            state = snackBarState,
            duration = 3000L
        )
    }
}
