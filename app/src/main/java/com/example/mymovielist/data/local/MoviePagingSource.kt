package com.example.mymovielist.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mymovielist.data.dto.MovieResult
import com.example.mymovielist.network.MovieApi
import okio.IOException
import retrofit2.HttpException

@ExperimentalStdlibApi
class MoviePagingSource(
    private val apiKey: String
) : PagingSource<Int, MovieResult>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MovieResult> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = MovieApi.retrofitService.getTopRated(apiKey, nextPageNumber)
            return LoadResult.Page(
                data = response.results,
                prevKey = null, // Only paging forward.
                nextKey = response.nextPage
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResult>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}