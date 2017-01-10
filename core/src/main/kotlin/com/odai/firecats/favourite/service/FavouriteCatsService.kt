package com.odai.firecats.favourite.service

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.event.Event
import com.odai.firecats.favourite.model.FavouriteCats
import io.reactivex.Flowable

interface FavouriteCatsService {

    fun getFavouriteCatsEvents(): Flowable<Event<FavouriteCats>>

    fun getFavouriteCats(): Flowable<FavouriteCats>

    fun addToFavourite(cat: Cat)

    fun removeFromFavourite(cat: Cat)

}
