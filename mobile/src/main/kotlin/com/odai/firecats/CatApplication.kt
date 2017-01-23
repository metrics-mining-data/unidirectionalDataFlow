package com.odai.firecats

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.odai.firecats.cat.service.CatService
import com.odai.firecats.cat.service.PersistedCatService
import com.odai.firecats.cats.service.CatsService
import com.odai.firecats.cats.service.PersistedCatsService
import com.odai.firecats.favourite.service.FavouriteCatsService
import com.odai.firecats.favourite.service.PersistedFavouriteCatsService
import com.odai.firecats.login.service.FirebaseLoginService
import com.odai.firecats.login.service.LoginService
import com.odai.firecats.persistence.CatRepository
import com.odai.firecats.persistence.FirebaseCatRepository
import com.odai.firecats.persistence.FirebaseLoginRepository
import com.odai.firecats.persistence.LoginRepository

class CatApplication : Application() {

    lateinit private var repository: CatRepository
    lateinit private var loginRepository: LoginRepository
    lateinit var catsService: CatsService
    lateinit var catService: CatService
    lateinit var favouriteCatsService: FavouriteCatsService
    lateinit var loginService: LoginService

    override fun onCreate() {
        super.onCreate()
        val firebaseApp = FirebaseApp.initializeApp(this, FirebaseOptions.fromResource(this), "FireCats")
        val firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp)
        val firebaseAuth = FirebaseAuth.getInstance(firebaseApp)
        firebaseDatabase.setPersistenceEnabled(true)
        repository = FirebaseCatRepository(firebaseDatabase.reference)
        loginRepository = FirebaseLoginRepository(firebaseAuth)
        catsService = PersistedCatsService(repository)
        catService = PersistedCatService(catsService)
        favouriteCatsService = PersistedFavouriteCatsService(repository)
        loginService = FirebaseLoginService(loginRepository)
    }

}
