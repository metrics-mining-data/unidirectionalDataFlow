package com.odai.architecturedemo

import android.app.Application
import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.api.FakeCatsApi
import com.odai.architecturedemo.cat.usecase.AsyncCatUseCase
import com.odai.architecturedemo.cat.usecase.CatUseCase
import com.odai.architecturedemo.cat.usecase.PersistedCatUseCase
import com.odai.architecturedemo.cats.usecase.*
import com.odai.architecturedemo.favourite.usecase.AsyncFavouriteCatsUseCase
import com.odai.architecturedemo.favourite.usecase.FavouriteCatsUseCase
import com.odai.architecturedemo.favourite.usecase.PersistedFavouriteCatsUseCase
import com.odai.architecturedemo.persistence.CatRepository
import com.odai.architecturedemo.persistence.InMemoryCatRepo

class CatApplication : Application() {

    private val api: CatApi = FakeCatsApi()
    private val freshnessChecker: CatsFreshnessChecker = AlwaysOutdatedCatsFreshnessChecker()
    private val repository: CatRepository = InMemoryCatRepo()

    val catsUseCase: CatsUseCase = AsyncCatsUseCase(PersistedCatsUseCase(api, repository, freshnessChecker))
    val catUseCase: CatUseCase = AsyncCatUseCase(PersistedCatUseCase(catsUseCase))
    val favouriteCatsUseCase: FavouriteCatsUseCase = AsyncFavouriteCatsUseCase(PersistedFavouriteCatsUseCase(api, repository, freshnessChecker))

}
