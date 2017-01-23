package com.odai.firecats.persistence

import com.odai.firecats.cats.model.Cats
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.login.model.User
import io.reactivex.Flowable

interface CatRepository {

    fun observeCats(): Flowable<Cats>

    fun observeFavouriteCats(user: User): Flowable<FavouriteCats>

    fun saveCatFavoriteStatus(user: User, it: Pair<Int, FavouriteState>)

}
