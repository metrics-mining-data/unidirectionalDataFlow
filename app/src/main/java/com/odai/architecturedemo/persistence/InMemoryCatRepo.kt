package com.odai.architecturedemo.persistence

import com.odai.architecturedemo.model.Cat
import com.odai.architecturedemo.model.Cats
import rx.Observable
import java.util.concurrent.TimeUnit

class InMemoryCatRepo : CatRepository {

    var cats: Cats = Cats(emptyList())
    var favouriteCats: Cats = Cats(emptyList())

    override fun saveCats(cats: Cats) {
        this.cats = cats
    }

    override fun readCats(): Observable<Cats> {
        if (cats.isEmpty()) {
            return Observable.empty()
        } else {
            return Observable.just(cats).delay(1, TimeUnit.SECONDS)
        }
    }

    override fun readFavouriteCats(): Observable<Cats> {
        if (favouriteCats.isEmpty()) {
            return Observable.empty()
        } else {
            return Observable.just(favouriteCats).delay(1, TimeUnit.SECONDS)
        }
    }

    override fun addToFavourite(cat: Cat) {
        favouriteCats = favouriteCats.add(cat)
    }

    override fun removeFromFavourite(cat: Cat) {
        favouriteCats = favouriteCats.remove(cat)
    }

    override fun saveFavouriteCats(cats: Cats) {
        favouriteCats = cats
    }

}
