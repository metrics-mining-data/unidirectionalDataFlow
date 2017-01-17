package com.odai.firecats

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import com.odai.firecats.cat.service.CatService
import com.odai.firecats.cat.service.PersistedCatService
import com.odai.firecats.cats.service.CatsService
import com.odai.firecats.cats.service.PersistedCatsService
import com.odai.firecats.favourite.service.FavouriteCatsService
import com.odai.firecats.favourite.service.PersistedFavouriteCatsService
import com.odai.firecats.persistence.CatRepository
import com.odai.firecats.persistence.FirebaseCatRepository

class CatApplication : Application() {

    lateinit private var repository: CatRepository
    lateinit var catsService: CatsService
    lateinit var catService: CatService
    lateinit var favouriteCatsService: FavouriteCatsService

    override fun onCreate() {
        super.onCreate()
        val firebaseApp = FirebaseApp.initializeApp(this, FirebaseOptions.fromResource(this), "FireCats")
        val firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp)
        firebaseDatabase.setPersistenceEnabled(true)
        repository = FirebaseCatRepository(firebaseDatabase.reference)
        catsService = PersistedCatsService(repository)
        catService = PersistedCatService(catsService)
        favouriteCatsService = PersistedFavouriteCatsService(repository)
    }

}
