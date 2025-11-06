package org.yourappdev.homeinterior.ui.Account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import homeinterior.composeapp.generated.resources.Res
import homeinterior.composeapp.generated.resources.arrow_back_
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.arrow_back_),
                colorFilter = ColorFilter.tint(color = Color(0xFF808080)),
                contentDescription = "Back",
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onBackClick() }
            )

            Text(
                text = "Profile",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C2C2C)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        ProfileHeader()

        Spacer(modifier = Modifier.height(60.dp))

        ProfileMenuItems()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.3.dp, Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFA3B18A))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Home Interior AI",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF615E5E),
            textAlign = TextAlign.Center
        )

        Text(
            text = "@admin",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF686666),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileMenuItems() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        ProfileMenuItem(
            label = "Username",
            value = "Home Interior AI"
        )

        ProfileMenuItem(
            label = "Email",
            value = "@admin"
        )

        ProfileMenuItem(
            label = "Restore Purchases",
            value = null
        )

        ProfileMenuItem(
            label = "Sign Out",
            value = null
        )

        Column {
            Text(
                text = "Delete Account",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFB5C5C)
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    label: String,
    value: String?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = if (value != null) 16.sp else 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4D4D4D)
            )

            if (value != null) {
                Text(
                    text = value,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF726F6F)
                )
            }
        }

        Divider(
            color = Color(0xFFE4E4E4),
            thickness = 0.5.dp
        )
    }
}