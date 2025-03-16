package com.finblue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.finblue.common.ScrollTopBar
import com.finblue.domain.model.FeedWrapper
import com.finblue.domain.model.Movie
import com.finblue.theme.Teal200
import com.finblue.utils.Dimens.FB_10_dp
import com.finblue.utils.Dimens.FB_120_dp
import com.finblue.utils.Dimens.FB_12_dp
import com.finblue.utils.Dimens.FB_16_dp
import com.finblue.utils.Dimens.FB_180_dp
import com.finblue.utils.Dimens.FB_20_dp
import com.finblue.utils.Dimens.FB_220_dp
import com.finblue.utils.Dimens.FB_2_dp
import com.finblue.utils.Dimens.FB_32_dp
import com.finblue.utils.Dimens.FB_36_dp
import com.finblue.utils.Dimens.FB_4_dp
import com.finblue.utils.Dimens.FB_6_dp
import com.finblue.utils.getMoreItemText
import com.finblue.utils.pagerTransition
import com.finblue.utils.resolve


@Composable
fun FeedScreen(
    collection: List<FeedWrapper>,
) {
    ScrollTopBar {
        FeedCollectionList(
            collection,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
private fun FeedCollectionList(collection: List<FeedWrapper>, modifier: Modifier = Modifier) {
    LazyColumn {
        item {
            PagerItemContainer(
                item = collection.first(),
                modifier = modifier
            )
        }
        itemsIndexed(collection.drop(1)) { index, feedCollection ->
            FeedCollection(
                feedCollection = feedCollection,
                index = index
            )
        }
    }
}

@Composable
private fun PagerItemContainer(item: FeedWrapper, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { item.feeds.size })
    Header(title = item.sortTypeStringDesc.resolve(), modifier = modifier)
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = FB_16_dp),
    ) { page ->
        with(item.feeds[page]) {
            TrendingItem(
                title = name,
                imageUrl = backdropPath,
                releaseDate = releaseDate,
                modifier =
                Modifier.pagerTransition(
                    pagerState = pagerState,
                    page = page,
                ),
            )
        }
    }

    Spacer(modifier = Modifier.height(FB_20_dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Teal200
            Box(
                modifier =
                Modifier
                    .padding(FB_4_dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(FB_6_dp),
            )
        }
    }
}

@Composable
private fun TrendingItem(
    title: String,
    imageUrl: String?,
    releaseDate: String?,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
        modifier
            .fillMaxWidth()
            .height(FB_180_dp)
            .clip(RoundedCornerShape(FB_10_dp))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier =
                Modifier
                    .padding(
                        start = FB_12_dp,
                        bottom = FB_6_dp,
                    )
                    .align(Alignment.BottomStart),
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(FB_6_dp))
                releaseDate?.let { releaseDate ->
                    Text(
                        text = releaseDate,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
private fun FeedCollection(
    feedCollection: FeedWrapper,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(top = FB_32_dp)) {
        Header(title = feedCollection.sortTypeStringDesc.resolve())
        Feeds(feedCollection.feeds, index)
    }
}

@Composable
private fun Header(title: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
        modifier
            .heightIn(min = FB_36_dp)
            .padding(start = FB_12_dp),
    ) {
        Text(
            text = title,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onSurface,
            modifier =
            Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
        )
        Text(
            text = getMoreItemText(),
            color = MaterialTheme.colorScheme.onSurface,
            modifier =
            Modifier
                .align(Alignment.CenterVertically)
                .padding(FB_12_dp)
        )
    }
}

@Composable
private fun Feeds(feeds: List<Movie>, index: Int, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = FB_2_dp, end = FB_2_dp),
    ) {
        items(feeds) { feed ->
            if (index % 3 == 0) {
                FBCard(feed, feed.backdropPath, FB_220_dp)
            } else {
                FBCard(feed, feed.posterPath, FB_120_dp)
            }
        }
    }
}

@Composable
private fun FBCard(
    movie: Movie,
    imageUrl: String?,
    itemWidth: Dp
) {
    Card(
        modifier =
        Modifier
            .padding(FB_6_dp),
        shape = RoundedCornerShape(FB_10_dp),
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = movie.name,
                modifier =
                Modifier
                    .size(width = itemWidth, height = FB_180_dp),
                contentScale = ContentScale.Crop,
            )
            Text(
                text = movie.name,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier =
                Modifier
                    .size(width = itemWidth, height = FB_36_dp)
                    .wrapContentHeight(),
            )
        }
    }
}