package com.odai.architecturedemo.persistence

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.favourite.model.FavouriteCats
import com.odai.architecturedemo.favourite.model.FavouriteState
import rx.Observable
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class InMemoryCatRepo : CatRepository {

    var cats: Cats = Cats(listOf(Cat(1, "Foo")))
    var favouriteCats: FavouriteCats = FavouriteCats(mapOf())

    override fun saveCats(cats: Cats) {
        this.cats = cats
    }

    override fun readCats(): Observable<Cats> {
        if (cats.isEmpty()) {
            return Observable.empty()
        } else {
            return Observable.just(cats).delay(500, TimeUnit.MILLISECONDS, Schedulers.immediate())
        }
    }

    override fun readFavouriteCats(): Observable<FavouriteCats> {
        if (favouriteCats.isEmpty()) {
            return Observable.empty()
        } else {
            return Observable.just(favouriteCats).delay(500, TimeUnit.MILLISECONDS, Schedulers.immediate())
        }
    }

    override fun saveFavouriteCats(cats: FavouriteCats) {
        favouriteCats = cats
    }

    override fun saveCatFavoriteStatus(it: Pair<Cat, FavouriteState>) {
        favouriteCats = favouriteCats.put(it)
    }

}
