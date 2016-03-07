package com.odai.architecturedemo.UseCase

import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.event.*
import com.odai.architecturedemo.model.Cat
import com.odai.architecturedemo.model.Cats
import com.odai.architecturedemo.model.FavouriteCats
import com.odai.architecturedemo.model.FavouriteState
import com.odai.architecturedemo.persistence.CatRepository
import rx.Observable
import rx.Observer
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject

class CatUseCase(val api: CatApi, val repository: CatRepository) {

    val catsSubject: BehaviorSubject<Event<Cats>> = BehaviorSubject.create(Event<Cats>(Status.IDLE, null, null))
    val favouriteCatsSubject: BehaviorSubject<Event<FavouriteCats>> = BehaviorSubject.create(Event<FavouriteCats>(Status.IDLE, null, null))

    fun getCatsEvents(): Observable<Event<Cats>> {
        if (isNotInitialised(catsSubject)) {
            repository.readCats()
                    .flatMap {
                        if (isOutdated(it)) {
                            fetchRemoteCats().startWith(it)
                        } else {
                            Observable.just(it)
                        }
                    }
                    .switchIfEmpty(fetchRemoteCats())
                    .compose(asEvent<Cats>())
                    .subscribeOn(Schedulers.io())
                    .subscribe { catsSubject.onNext(it) }
        }
        return catsSubject.asObservable()
    }

    fun getCats() = getCatsEvents().compose(asData<Cats>())

    private fun isOutdated(it: Cats): Boolean {
        return true;
    }

    private fun fetchRemoteCats() = api.getCats()
            .doOnNext { repository.saveCats(it) }

    fun getFavouriteCatsEvents(): Observable<Event<FavouriteCats>> {
        if (isNotInitialised(favouriteCatsSubject)) {
            repository.readFavouriteCats()
                    .flatMap {
                        if (isOutdated(it)) {
                            fetchRemoteFavouriteCats().startWith(it)
                        } else {
                            Observable.just(it)
                        }
                    }
                    .switchIfEmpty(fetchRemoteFavouriteCats())
                    .compose(asEvent<FavouriteCats>())
                    .subscribeOn(Schedulers.io())
                    .subscribe { favouriteCatsSubject.onNext(it) }
        }
        return favouriteCatsSubject.asObservable()
    }

    fun getFavouriteCats() = getFavouriteCatsEvents().compose(asData<FavouriteCats>())

    private fun isOutdated(it: FavouriteCats): Boolean {
        return true;
    }

    private fun fetchRemoteFavouriteCats(): Observable<FavouriteCats> {
        return api.getFavouriteCats()
                .map {
                    val map = it.list.fold(mapOf<Cat, FavouriteState>()) { map, cat -> map.plus(Pair(cat, FavouriteState.FAVOURITE)) }
                    FavouriteCats(map)
                }
                .doOnNext { repository.saveFavouriteCats(it) }
    }

    fun addToFavourite(cat: Cat) {
        api.addToFavourite(cat)
                .map { Pair(cat, FavouriteState.FAVOURITE) }
                .onErrorReturn { Pair(cat, FavouriteState.UN_FAVOURITE) }
                .startWith(Pair(cat, FavouriteState.PENDING_FAVOURITE))
                .doOnNext { repository.saveCatFavoriteStatus(it) }
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<Pair<Cat, FavouriteState>> {

                    override fun onNext(p0: Pair<Cat, FavouriteState>) {
                        val value = favouriteCatsSubject.value
                        val favouriteCats = value.data ?: FavouriteCats(mapOf())
                        favouriteCatsSubject.onNext(Event(value.status, favouriteCats.put(p0), value.error))
                    }

                    override fun onError(p0: Throwable?) {
                        throw UnsupportedOperationException()
                    }

                    override fun onCompleted() {
                    }

                })
    }

    fun removeFromFavourite(cat: Cat) {
        api.addToFavourite(cat)
                .map { Pair(cat, FavouriteState.UN_FAVOURITE) }
                .onErrorReturn { Pair(cat, FavouriteState.FAVOURITE) }
                .startWith(Pair(cat, FavouriteState.PENDING_UN_FAVOURITE))
                .doOnNext { repository.saveCatFavoriteStatus(it) }
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<Pair<Cat, FavouriteState>> {

                    override fun onNext(p0: Pair<Cat, FavouriteState>) {
                        val value = favouriteCatsSubject.value
                        val favouriteCats = value.data ?: FavouriteCats(mapOf())
                        favouriteCatsSubject.onNext(Event(value.status, favouriteCats.put(p0), value.error))
                    }

                    override fun onError(p0: Throwable?) {
                        throw UnsupportedOperationException()
                    }

                    override fun onCompleted() {
                    }

                })
    }


}
