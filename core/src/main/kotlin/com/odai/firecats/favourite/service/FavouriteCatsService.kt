package com.odai.firecats.favourite.service

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.event.Event
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.login.model.User
import io.reactivex.Flowable

interface FavouriteCatsService {

    fun getFavouriteCatsEvents(user: User): Flowable<Event<FavouriteCats>>

    fun getFavouriteCats(user: User): Flowable<FavouriteCats>

    fun addToFavourite(user: User, cat: Cat)

    fun removeFromFavourite(user: User, cat: Cat)

}
