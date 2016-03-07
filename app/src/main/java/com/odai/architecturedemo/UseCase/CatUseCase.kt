package com.odai.architecturedemo.UseCase

import com.odai.architecturedemo.api.CatApi
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

    val catsSubject :BehaviorSubject<Cats> = BehaviorSubject.create()
    val favouriteCatsSubject :BehaviorSubject<FavouriteCats> = BehaviorSubject.create()

    fun getCats(): Observable<Cats> {
        if (!catsSubject.hasValue()) {
            repository.readCats()
                .switchIfEmpty(
                        api.getCats()
                            .doOnNext { repository.saveCats(it) }
                )
                .subscribeOn(Schedulers.io())
                .subscribe { catsSubject.onNext(it) }
        }
        return catsSubject.asObservable()
    }

    fun getFavouriteCats(): Observable<FavouriteCats> {
        if (!favouriteCatsSubject.hasValue()) {
            repository.readFavouriteCats()
                    .switchIfEmpty(
                            api.getFavouriteCats()
                                    .map {
                                        val map = it.list.fold(mapOf<Cat, FavouriteState>()) { map, cat -> map.plus(Pair(cat, FavouriteState.FAVOURITE)) }
                                        FavouriteCats(map)
                                    }
                                    .doOnNext { repository.saveFavouriteCats(it) }
                    )
                    .subscribeOn(Schedulers.io())
                    .subscribe { favouriteCatsSubject.onNext(it) }
        }
        return favouriteCatsSubject.asObservable()
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
                    favouriteCatsSubject.onNext(favouriteCatsSubject.value.put(p0))
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
                        favouriteCatsSubject.onNext(favouriteCatsSubject.value.put(p0))
                    }

                    override fun onError(p0: Throwable?) {
                        throw UnsupportedOperationException()
                    }

                    override fun onCompleted() {
                    }

                })
    }


}
