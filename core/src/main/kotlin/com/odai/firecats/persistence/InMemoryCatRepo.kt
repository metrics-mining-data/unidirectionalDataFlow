package com.odai.firecats.persistence

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.net.URI
import java.util.concurrent.TimeUnit

class InMemoryCatRepo : CatRepository {

    private var cats: Cats = Cats(listOf(
            Cat(404, "Not Found", URI.create("https://http.cat/404")),
            Cat(411, "Length Required", URI.create("https://http.cat/411")),
            Cat(418, "I'm a Teapot", URI.create("https://http.cat/418")),
            Cat(500, "Internal Server Error", URI.create("https://http.cat/500"))
    ))
    private var favouriteCats: FavouriteCats = FavouriteCats(mapOf())

    override fun saveCats(cats: Cats) {
        this.cats = cats
    }

    override fun readCats(): Flowable<Cats> {
        if (cats.isEmpty()) {
            return Flowable.empty()
        } else {
            return Flowable.just(cats).delay(500, TimeUnit.MILLISECONDS, Schedulers.trampoline())
        }
    }

    override fun readFavouriteCats(): Flowable<FavouriteCats> {
        if (favouriteCats.isEmpty()) {
            return Flowable.empty()
        } else {
            return Flowable.just(favouriteCats).delay(500, TimeUnit.MILLISECONDS, Schedulers.trampoline())
        }
    }

    override fun saveFavouriteCats(cats: FavouriteCats) {
        favouriteCats = cats
    }

    override fun saveCatFavoriteStatus(it: Pair<Cat, FavouriteState>) {
        favouriteCats = favouriteCats.put(it)
    }

}
