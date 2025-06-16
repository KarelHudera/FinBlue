package com.finblue.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.finblue.utils.Dimens.FB_10_dp
import com.finblue.utils.Dimens.FB_180_dp

@Composable
fun AddPortfolioCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(FB_180_dp)
            .clip(RoundedCornerShape(FB_10_dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(FB_10_dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x220055AA)), // Light faded blue background
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0055AA),
            )
        }
    }
}