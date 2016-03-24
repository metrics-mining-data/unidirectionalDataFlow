package com.odai.architecturedemo.favourite.usecase

import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.cats.usecase.CatsFreshnessChecker
import com.odai.architecturedemo.event.*
import com.odai.architecturedemo.favourite.model.FavouriteCats
import com.odai.architecturedemo.favourite.model.FavouriteState
import com.odai.architecturedemo.persistence.CatRepository
import rx.Observable
import rx.Observer
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject

class PersistedFavouriteCatsUseCase(val api: CatApi, val repository: CatRepository, val freshnessChecker: CatsFreshnessChecker): FavouriteCatsUseCase {

    val favouriteCatsSubject: BehaviorSubject<Event<FavouriteCats>> = BehaviorSubject.create(Event<FavouriteCats>(Status.IDLE, null, null))

    override fun getFavouriteCatsEvents(): Observable<Event<FavouriteCats>> {
        return favouriteCatsSubject.asObservable()
                .startWith(initialiseSubject())
                .distinctUntilChanged()
    }

    override fun getFavouriteCats() = getFavouriteCatsEvents().compose(asData())

    private fun initialiseSubject(): Observable<Event<FavouriteCats>> {
        if (isInitialised(favouriteCatsSubject)) {
            return Observable.empty()
        }
        return repository.readFavouriteCats()
                .flatMap { updateFromRemoteIfOutdated(it) }
                .switchIfEmpty(fetchRemoteFavouriteCats())
                .compose(asEvent<FavouriteCats>())
                .doOnNext { favouriteCatsSubject.onNext(it) }
    }

    private fun updateFromRemoteIfOutdated(it: FavouriteCats): Observable<FavouriteCats>? {
        return if (freshnessChecker.isFresh(it)) {
            Observable.just(it)
        } else {
            fetchRemoteFavouriteCats().startWith(it)
        }
    }

    private fun fetchRemoteFavouriteCats(): Observable<FavouriteCats> {
        return api.getFavouriteCats()
                .map { asFavouriteCats(it) }
                .doOnNext { repository.saveFavouriteCats(it) }
    }

    private fun asFavouriteCats(it: Cats): FavouriteCats {
        return FavouriteCats(
                it.list.fold(mapOf<Cat, FavouriteState>()) { map, cat ->
                    map.plus(Pair(cat, FavouriteState.FAVOURITE))
                }
        )
    }

    override fun addToFavourite(cat: Cat) {
        api.addToFavourite(cat)
                .map { Pair(cat, FavouriteState.FAVOURITE) }
                .onErrorReturn { Pair(cat, FavouriteState.UN_FAVOURITE) }
                .startWith(Pair(cat, FavouriteState.PENDING_FAVOURITE))
                .doOnNext { repository.saveCatFavoriteStatus(it) }
                .subscribe(favouriteCatStateObserver)
    }

    override fun removeFromFavourite(cat: Cat) {
        api.removeFromFavourite(cat)
                .map { Pair(cat, FavouriteState.UN_FAVOURITE) }
                .onErrorReturn { Pair(cat, FavouriteState.FAVOURITE) }
                .startWith(Pair(cat, FavouriteState.PENDING_UN_FAVOURITE))
                .doOnNext { repository.saveCatFavoriteStatus(it) }
                .subscribe(favouriteCatStateObserver)
    }

    val favouriteCatStateObserver = object : Observer<Pair<Cat, FavouriteState>> {

        override fun onNext(p0: Pair<Cat, FavouriteState>) {
            val value = favouriteCatsSubject.value
            val favouriteCats = value.data ?: FavouriteCats(mapOf())
            favouriteCatsSubject.onNext(Event(value.status, favouriteCats.put(p0), value.error))
        }

        override fun onError(p0: Throwable?) {
            throw UnsupportedOperationException("Error on favourite state pipeline. This should never happen", p0)
        }

        override fun onCompleted() {
            // We don't want to finish the subject after a single favourite action so we don't do anything here
        }

    }


}
