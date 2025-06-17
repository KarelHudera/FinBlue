package com.finblue.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.finblue.utils.Dimens.FB_10_dp
import com.finblue.utils.Dimens.FB_12_dp
import com.finblue.utils.Dimens.FB_180_dp
import com.finblue.utils.Dimens.FB_6_dp

@Composable
fun PortfolioItem(
    title: String,
    currency: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF00AFFF),
            Color(0xFF0055AA)
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(FB_180_dp)
            .clip(RoundedCornerShape(FB_10_dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = FB_12_dp, bottom = FB_6_dp)
                    .align(Alignment.BottomStart),
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(FB_6_dp))
                Text(
                    text = currency,
                    color = Color.White,
                )
            }
        }
    }
}