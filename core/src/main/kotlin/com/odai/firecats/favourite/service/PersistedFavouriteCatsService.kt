package com.odai.firecats.favourite.service

import com.jakewharton.rxrelay2.BehaviorRelay
import com.odai.firecats.api.CatApi
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.cats.service.CatsFreshnessChecker
import com.odai.firecats.event.*
import com.odai.firecats.favourite.model.ActionState
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.favourite.model.FavouriteStatus
import com.odai.firecats.persistence.CatRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.Consumer

class PersistedFavouriteCatsService(
        private val api: CatApi,
        private val repository: CatRepository,
        private val freshnessChecker: CatsFreshnessChecker
) : FavouriteCatsService {

    private val favouriteCatsRelay: BehaviorRelay<Event<FavouriteCats>> = BehaviorRelay.createDefault(Event<FavouriteCats>(Status.IDLE, null, null))

    override fun getFavouriteCatsEvents(): Flowable<Event<FavouriteCats>> {
        return favouriteCatsRelay.toFlowable(BackpressureStrategy.LATEST)
                .startWith(initialiseSubject())
                .distinctUntilChanged()
    }

    override fun getFavouriteCats(): Flowable<FavouriteCats> = getFavouriteCatsEvents().compose(asData())

    private fun initialiseSubject(): Flowable<Event<FavouriteCats>> {
        if (isInitialised(favouriteCatsRelay)) {
            return Flowable.empty()
        }
        return repository.readFavouriteCats()
                .flatMap { updateFromRemoteIfOutdated(it) }
                .switchIfEmpty(fetchRemoteFavouriteCats())
                .compose(asEvent<FavouriteCats>())
                .doOnNext { favouriteCatsRelay.accept(it) }
    }

    private fun updateFromRemoteIfOutdated(it: FavouriteCats): Flowable<FavouriteCats>? {
        return if (freshnessChecker.isFresh(it)) {
            Flowable.just(it)
        } else {
            fetchRemoteFavouriteCats().startWith(it)
        }
    }

    private fun fetchRemoteFavouriteCats(): Flowable<FavouriteCats> {
        return api.getFavouriteCats()
                .map { asFavouriteCats(it) }
                .doOnNext { repository.saveFavouriteCats(it) }
    }

    private fun asFavouriteCats(it: Cats): FavouriteCats {
        return FavouriteCats(
                it.fold(mapOf<Cat, FavouriteState>()) { map, cat ->
                    map.plus(Pair(cat, FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)))
                }
        )
    }

    override fun addToFavourite(cat: Cat) {
        api.addToFavourite(cat)
                .map { Pair(cat, FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)) }
                .onErrorReturn { Pair(cat, FavouriteState(FavouriteStatus.UN_FAVOURITE, ActionState.CONFIRMED)) }
                .startWith(Pair(cat, FavouriteState(FavouriteStatus.FAVOURITE, ActionState.PENDING)))
                .doOnNext { repository.saveCatFavoriteStatus(it) }
                .subscribe(favouriteCatStateObserver)
    }

    override fun removeFromFavourite(cat: Cat) {
        api.removeFromFavourite(cat)
                .map { Pair(cat, FavouriteState(FavouriteStatus.UN_FAVOURITE, ActionState.CONFIRMED)) }
                .onErrorReturn { Pair(cat, FavouriteState(FavouriteStatus.FAVOURITE, ActionState.CONFIRMED)) }
                .startWith(Pair(cat, FavouriteState(FavouriteStatus.UN_FAVOURITE, ActionState.PENDING)))
                .doOnNext { repository.saveCatFavoriteStatus(it) }
                .subscribe(favouriteCatStateObserver)
    }

    private val favouriteCatStateObserver = object : Consumer<Pair<Cat, FavouriteState>> {

        override fun accept(p0: Pair<Cat, FavouriteState>) {
            val value = favouriteCatsRelay.value
            val favouriteCats = value.data ?: FavouriteCats(mapOf())
            favouriteCatsRelay.accept(Event(value.status, favouriteCats.put(p0), value.error))
        }

    }


}
