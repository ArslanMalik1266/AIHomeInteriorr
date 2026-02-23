package org.yourappdev.homeinterior.ui.authentication.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.emailicon
import homeinterior.composeapp.generated.resources.google
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.yourappdev.homeinterior.navigation.Routes
import org.yourappdev.homeinterior.ui.UiUtils.ButtonWithIcon
import org.yourappdev.homeinterior.ui.UiUtils.ClickableText
import org.yourappdev.homeinterior.ui.theme.buttonBack
import org.yourappdev.homeinterior.ui.theme.smallText

@Preview(showBackground = true)
@Composable
fun WelcomeScreen(
    onContinueWithGoogle: () -> Unit = {},
    onContinueWithEmail: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Welcome to",
            fontSize = 16.sp,
            color = smallText
        )

        Text(
            text = "Home Interior AI",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9CA986)
        )

        Text(
            text = "Interiors shaped by your imagination, perfected by AI.",
            fontSize = 14.sp,
            color = smallText,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Let's Get Started...",
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )

        Spacer(modifier = Modifier.height(28.dp))

        ButtonWithIcon(image = Res.drawable.google, borderColor = buttonBack, title = "Continue with Google",
            onClick = fun() {

            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ButtonWithIcon(image = Res.drawable.emailicon, title = "Continue with Email",     onClick = {
            onLogin()
        }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Login link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account? ",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 1.sp,
                fontWeight = FontWeight.Normal,
            )
            ClickableText(title = "Login", textSize = 14.sp, fontWeight = FontWeight.Bold) {
                onLogin()
            }
        }
    }
}


