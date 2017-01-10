package com.odai.firecats

import android.app.Application
import com.odai.firecats.api.CatApi
import com.odai.firecats.api.FakeCatsApi
import com.odai.firecats.cat.service.AsyncCatService
import com.odai.firecats.cat.service.CatService
import com.odai.firecats.cat.service.PersistedCatService
import com.odai.firecats.cats.service.*
import com.odai.firecats.favourite.service.AsyncFavouriteCatsService
import com.odai.firecats.favourite.service.FavouriteCatsService
import com.odai.firecats.favourite.service.PersistedFavouriteCatsService
import com.odai.firecats.persistence.CatRepository
import com.odai.firecats.persistence.InMemoryCatRepo

class CatApplication : Application() {

    private val api: CatApi = FakeCatsApi()
    private val freshnessChecker: CatsFreshnessChecker = AlwaysOutdatedCatsFreshnessChecker()
    private val repository: CatRepository = InMemoryCatRepo()

    val catsService: CatsService = AsyncCatsService(PersistedCatsService(api, repository, freshnessChecker))
    val catService: CatService = AsyncCatService(PersistedCatService(catsService))
    val favouriteCatsService: FavouriteCatsService = AsyncFavouriteCatsService(PersistedFavouriteCatsService(api, repository, freshnessChecker))

}
