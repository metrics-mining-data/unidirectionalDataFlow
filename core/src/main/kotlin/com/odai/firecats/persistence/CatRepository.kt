package com.odai.firecats.persistence

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import io.reactivex.Flowable

interface CatRepository {

    fun saveCats(cats: Cats)

    fun readCats(): Flowable<Cats>

    fun readFavouriteCats(): Flowable<FavouriteCats>

    fun saveFavouriteCats(cats: FavouriteCats)

    fun saveCatFavoriteStatus(it: Pair<Cat, FavouriteState>)

}
