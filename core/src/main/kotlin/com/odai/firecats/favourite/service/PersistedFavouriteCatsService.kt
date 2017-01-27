package com.odai.firecats.favourite.service

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.event.Event
import com.odai.firecats.event.asData
import com.odai.firecats.event.asEvent
import com.odai.firecats.favourite.model.ActionState
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.favourite.model.FavouriteStatus
import com.odai.firecats.login.model.User
import com.odai.firecats.persistence.CatRepository
import io.reactivex.Flowable

class PersistedFavouriteCatsService(
        private val repository: CatRepository
) : FavouriteCatsService {

    override fun getFavouriteCatsEvents(user: User): Flowable<Event<FavouriteCats>> {
        return repository.observeFavouriteCats(user).compose(asEvent())
    }

    override fun getFavouriteCats(user: User): Flowable<FavouriteCats> = getFavouriteCatsEvents(user).compose(asData())

    override fun addToFavourite(user: User, cat: Cat) {
        //TODO Deal with pending server side
        val favouriteState = FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)
        repository.saveCatFavoriteStatus(user, Pair(cat.id, favouriteState))
    }

    override fun removeFromFavourite(user: User, cat: Cat) {
        //TODO Deal with pending server side
        repository.saveCatFavoriteStatus(user, Pair(cat.id, FavouriteState(FavouriteStatus.UN_FAVOURITE, ActionState.CONFIRMED)))
    }

}
