package com.odai.architecturedemo.favourite.service

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.event.Event
import com.odai.architecturedemo.favourite.model.FavouriteCats
import io.reactivex.Flowable

interface FavouriteCatsService {

    fun getFavouriteCatsEvents(): Flowable<Event<FavouriteCats>>

    fun getFavouriteCats(): Flowable<FavouriteCats>

    fun addToFavourite(cat: Cat)

    fun removeFromFavourite(cat: Cat)

}
