package com.example.mymovielist

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.example.mymovielist.data.MovieDataSource
import com.example.mymovielist.data.local.LocalDB
import com.example.mymovielist.data.local.MoviesDatabase
import com.example.mymovielist.data.local.MoviesLocalRepository
import com.example.mymovielist.ui.browse.BrowseViewModel
import com.example.mymovielist.ui.mylist.MyListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

@ExperimentalPagingApi
@ExperimentalStdlibApi
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            viewModel {
                parameters ->
                BrowseViewModel(
                    get(),
                    get() as MovieDataSource,
                    savedStateHandle = parameters.get()
                )
            }

            viewModel {
                MyListViewModel(
                    get()
                )
            }

            single { MoviesLocalRepository(MoviesDatabase.getInstance(this@MyApp)) as MovieDataSource }
            single { LocalDB.createMoviesDao(this@MyApp) }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}