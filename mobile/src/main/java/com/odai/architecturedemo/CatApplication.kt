package com.odai.architecturedemo

import android.app.Application
import com.odai.architecturedemo.cats.usecase.CatsUseCase
import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.api.FakeCatsApi
import com.odai.architecturedemo.favourite.usecase.FavouriteCatsUseCase
import com.odai.architecturedemo.persistence.CatRepository
import com.odai.architecturedemo.persistence.InMemoryCatRepo

class CatApplication : Application() {

    private val api : CatApi = FakeCatsApi()
    private val repository : CatRepository = InMemoryCatRepo()

    val catsUseCase: CatsUseCase = CatsUseCase(api, repository)
    val favouriteCatsUseCase: FavouriteCatsUseCase = FavouriteCatsUseCase(api, repository)

}
