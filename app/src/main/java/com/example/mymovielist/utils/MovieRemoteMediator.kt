package com.example.mymovielist.utils

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.mymovielist.data.dto.MovieResult
import com.example.mymovielist.data.dto.MovieType
import com.example.mymovielist.data.dto.MoviesPage
import com.example.mymovielist.data.local.MoviesDatabase
import com.example.mymovielist.data.local.RemoteKeys
import com.example.mymovielist.network.MovieApi
import okio.IOException
import retrofit2.HttpException

private const val MOVIE_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
@ExperimentalStdlibApi
class MovieRemoteMediator(
    private val movieType: MovieType,
    private val apiKey: String,
    private val movieDatabase: MoviesDatabase
) : RemoteMediator<Int, MovieResult>() {

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieResult>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: MOVIE_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val apiResponse: MoviesPage
            if (movieType == MovieType.TOP_RATED) {
                apiResponse = MovieApi.retrofitService.getMovies("top_rated", apiKey, page)
            } else if (movieType == MovieType.POPULAR) {
                apiResponse = MovieApi.retrofitService.getMovies("popular", apiKey, page)
            } else {
                apiResponse = MovieApi.retrofitService.getMovies("now_playing", apiKey, page)
            }
            val movies = apiResponse.results
            val endOfPaginationReached = movies.isEmpty()
            movieDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    movieDatabase.remoteKeysDao().clearRemoteKeys()
                    movieDatabase.movieDao().deleteAllMovies()
                }
                val prevKey = if (page == MOVIE_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = movies.map {
                    RemoteKeys(repoId = it.id.toLong(), prevKey = prevKey, nextKey = nextKey)
                }
                movieDatabase.remoteKeysDao().insertAll(keys)
                movieDatabase.movieDao().saveMovies(movies)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieResult>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                movieDatabase.remoteKeysDao().remoteKeysRepoId(repo.id.toLong())
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieResult>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                movieDatabase.remoteKeysDao().remoteKeysRepoId(repo.id.toLong())
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieResult>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                movieDatabase.remoteKeysDao().remoteKeysRepoId(repoId.toLong())
            }
        }
    }
}