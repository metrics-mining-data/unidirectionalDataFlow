package com.odai.architecturedemo.persistence

import com.odai.architecturedemo.model.Cat
import com.odai.architecturedemo.model.Cats
import com.odai.architecturedemo.model.FavouriteCats
import com.odai.architecturedemo.model.FavouriteState
import rx.Observable

interface CatRepository {

    fun saveCats(cats: Cats): Unit

    fun readCats(): Observable<Cats>

    fun readFavouriteCats(): Observable<FavouriteCats>

    fun saveFavouriteCats(cats: FavouriteCats)

    fun saveCatFavoriteStatus(it: Pair<Cat, FavouriteState>)

}
