package com.finblue.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.finblue.MR
import com.finblue.domain.model.Portfolio
import com.finblue.utils.Dimens.FB_16_dp
import com.finblue.utils.Dimens.FB_20_dp
import com.finblue.utils.Dimens.FB_4_dp
import com.finblue.utils.Dimens.FB_6_dp
import com.finblue.utils.pagerTransition

@Composable
fun PortfolioCarousel(
    portfolios: List<Portfolio>,
    onAddPortfolioClick: () -> Unit,
    onPortfolioClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { portfolios.size + 1 })

    Header(title = "Portfolios", modifier = modifier)

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = FB_16_dp),
    ) { page ->
        if (page < portfolios.size) {
            with(portfolios[page]) {
                PortfolioItem(
                    title = name,
                    currency = baseCurrency,
                    modifier = Modifier.pagerTransition(
                        pagerState = pagerState,
                        page = page,
                    ),
                    onClick = {
                        onPortfolioClick(id)
                    },
                )
            }
        } else {
            AddPortfolioCard(
                onClick = onAddPortfolioClick,
                modifier = Modifier.pagerTransition(
                    pagerState = pagerState,
                    page = page,
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(FB_20_dp))

    // Page indicator dots
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) {
                Color(0xFF0055AA)
            } else {
                Color.LightGray
            }
            Box(
                modifier = Modifier
                    .padding(FB_4_dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(FB_6_dp),
            )
        }
    }
}