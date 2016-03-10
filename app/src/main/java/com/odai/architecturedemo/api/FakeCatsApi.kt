package com.odai.architecturedemo.api

import android.util.Log
import com.odai.architecturedemo.cats.model.Cat
import com.odai.architecturedemo.cats.model.Cats
import rx.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class FakeCatsApi : CatApi {

    var favouriteCats = Cats(listOf(Cat("Bar")))

    override fun getFavouriteCats(): Observable<Cats> {
        return Observable.just(favouriteCats).delay(3, TimeUnit.SECONDS)
    }

    override fun addToFavourite(cat: Cat): Observable<Cat> {
        favouriteCats = favouriteCats.add(cat)
        return Observable.just(cat).delay(3, TimeUnit.SECONDS)
    }

    override fun removeFromFavourite(cat: Cat): Observable<Cat> {
        favouriteCats = favouriteCats.remove(cat)
        return Observable.just(cat).delay(3, TimeUnit.SECONDS)
    }

    override fun getCats(): Observable<Cats> {
        return Observable.just(Cats(listOf(Cat("Foo"), Cat("Bar")))).delay(3, TimeUnit.SECONDS)
    }

}
