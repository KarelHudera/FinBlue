package com.finblue.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.finblue.domain.model.Portfolio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioDropdown(
    portfolios: List<Portfolio>,
    selectedPortfolio: Portfolio?,
    onPortfolioSelected: (Portfolio) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedPortfolio?.name ?: "Select Portfolio",
            onValueChange = {},
            readOnly = true,
            label = { Text("Portfolio") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            portfolios.forEach { portfolio ->
                DropdownMenuItem(
                    text = { Text(portfolio.name) },
                    onClick = {
                        onPortfolioSelected(portfolio)
                        expanded = false
                    }
                )
            }
        }
    }
}