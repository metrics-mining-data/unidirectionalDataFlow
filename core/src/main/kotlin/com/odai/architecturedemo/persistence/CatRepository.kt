package com.odai.architecturedemo.persistence

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.favourite.model.FavouriteCats
import com.odai.architecturedemo.favourite.model.FavouriteState
import io.reactivex.Flowable

interface CatRepository {

    fun saveCats(cats: Cats)

    fun readCats(): Flowable<Cats>

    fun readFavouriteCats(): Flowable<FavouriteCats>

    fun saveFavouriteCats(cats: FavouriteCats)

    fun saveCatFavoriteStatus(it: Pair<Cat, FavouriteState>)

}
