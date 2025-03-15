package com.finblue.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import com.finblue.cache.Database
import com.finblue.data.response.Wrapper
import com.finblue.data.response.asDomainModel
import com.finblue.domain.repository.BaseFeedRepository
import com.finblue.utils.getNowPlayingText
import com.finblue.utils.getUpcomingText

class MovieFeedRepository(
    private val httpClient: HttpClient,
    database: Database,
    ioDispatcher: CoroutineDispatcher
) : BaseFeedRepository(database, ioDispatcher) {

    override suspend fun popularItems() =
        httpClient.get("movie/popular").body<Wrapper>().items.asDomainModel()

    override suspend fun nowPlayingItems() =
        httpClient.get("movie/now_playing").body<Wrapper>().items.asDomainModel()

    override suspend fun latestItems() =
        httpClient.get("movie/upcoming").body<Wrapper>().items.asDomainModel()

    override suspend fun topRatedItems() =
        httpClient.get("movie/top_rated").body<Wrapper>().items.asDomainModel()

    override suspend fun trendingItems() =
        httpClient.get("trending/movie/day").body<Wrapper>().items.asDomainModel()

    override suspend fun discoverItems() =
        httpClient.get("discover/movie").body<Wrapper>().items.asDomainModel()

    override fun getNowPlayingStringDesc() = getNowPlayingText()

    override fun getLatestStringDesc() = getUpcomingText()
}