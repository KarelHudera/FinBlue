package com.finblue.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.finblue.utils.Dimens.FB_12_dp
import com.finblue.utils.Dimens.FB_16_dp

@Composable
fun EmptyPortfolioMessage() {
    Text(
        text = "This portfolio has no assets.",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FB_12_dp, horizontal = FB_16_dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium
    )
}