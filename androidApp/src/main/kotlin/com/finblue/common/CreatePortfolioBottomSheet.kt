package com.finblue.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.finblue.domain.model.Portfolio
import com.finblue.utils.Dimens.FB_12_dp
import com.finblue.utils.Dimens.FB_16_dp
import com.finblue.utils.Dimens.FB_32_dp
import kotlinx.datetime.Clock
import java.util.UUID

@Composable
fun CreatePortfolioBottomSheet(
    onCreatePortfolio: (Portfolio) -> Unit,
    onDismiss: () -> Unit
) {
    var portfolioName by remember { mutableStateOf("") }
    var brokerBank by remember { mutableStateOf("") }
    var baseCurrency by remember { mutableStateOf("USD") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(FB_16_dp),
        verticalArrangement = Arrangement.spacedBy(FB_16_dp)
    ) {
        Text(
            text = "Create New Portfolio",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = portfolioName,
            onValueChange = { portfolioName = it },
            label = { Text("Portfolio Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = brokerBank,
            onValueChange = { brokerBank = it },
            label = { Text("Broker/Bank (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = baseCurrency,
            onValueChange = { baseCurrency = it },
            label = { Text("Base Currency") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(FB_12_dp)
        ) {
            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    if (portfolioName.isNotBlank()) {
                        val portfolio = Portfolio(
                            id = UUID.randomUUID().toString(),
                            name = portfolioName.trim(),
                            brokerBank = brokerBank.trim().takeIf { it.isNotBlank() },
                            baseCurrency = baseCurrency.trim(),
                            description = description.trim().takeIf { it.isNotBlank() },
                            createdAt = Clock.System.now().toEpochMilliseconds(),
                        )
                        onCreatePortfolio(portfolio)
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = portfolioName.isNotBlank()
            ) {
                Text("Create")
            }
        }

        // Add some bottom padding for the bottom sheet
        Spacer(modifier = Modifier.height(FB_32_dp))
    }
}