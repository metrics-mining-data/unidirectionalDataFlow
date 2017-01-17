package com.odai.firecats.favourite.service

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.event.Event
import com.odai.firecats.event.asData
import com.odai.firecats.event.asEvent
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.persistence.CatRepository
import io.reactivex.Flowable

class PersistedFavouriteCatsService(
        private val repository: CatRepository
) : FavouriteCatsService {

    override fun getFavouriteCatsEvents(): Flowable<Event<FavouriteCats>> {
        return repository.observeFavouriteCats().compose(asEvent())
    }

    override fun getFavouriteCats(): Flowable<FavouriteCats> = getFavouriteCatsEvents().compose(asData())

    override fun addToFavourite(cat: Cat) {
        /*api.addToFavourite(cat)
                .map { Pair(cat, FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)) }
                .onErrorReturn { Pair(cat, FavouriteState(FavouriteStatus.UN_FAVOURITE, ActionState.CONFIRMED)) }
                .startWith(Pair(cat, FavouriteState(FavouriteStatus.FAVOURITE, ActionState.PENDING)))
                .doOnNext { repository.saveCatFavoriteStatus(it) }
                .subscribe(favouriteCatStateObserver)*/
    }

    override fun removeFromFavourite(cat: Cat) {
        /*api.removeFromFavourite(cat)
                .map { Pair(cat, FavouriteState(FavouriteStatus.UN_FAVOURITE, ActionState.CONFIRMED)) }
                .onErrorReturn { Pair(cat, FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)) }
                .startWith(Pair(cat, FavouriteState(FavouriteStatus.UN_FAVOURITE, ActionState.PENDING)))
                .doOnNext { repository.saveCatFavoriteStatus(it) }
                .subscribe(favouriteCatStateObserver)*/
    }

}
