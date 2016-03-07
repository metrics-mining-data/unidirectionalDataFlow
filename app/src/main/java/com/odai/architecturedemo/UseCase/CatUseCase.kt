package com.odai.architecturedemo.UseCase

import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.model.Cat
import com.odai.architecturedemo.model.Cats
import com.odai.architecturedemo.persistence.CatRepository
import rx.Observable
import rx.Observer
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject

class CatUseCase(val api: CatApi, val repository: CatRepository) {

    val catsSubject :BehaviorSubject<Cats> = BehaviorSubject.create()
    val favouriteCatsSubject :BehaviorSubject<Cats> = BehaviorSubject.create()

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

    fun getFavouriteCats(): Observable<Cats> {
        if (!favouriteCatsSubject.hasValue()) {
            repository.readFavouriteCats()
                    .switchIfEmpty(
                            api.getFavouriteCats()
                                    .doOnNext { repository.saveFavouriteCats(it) }
                    )
                    .subscribeOn(Schedulers.io())
                    .subscribe { favouriteCatsSubject.onNext(it) }
        }
        return favouriteCatsSubject.asObservable()
    }

    fun addToFavourite(cat: Cat) {
        favouriteCatsSubject.onNext(favouriteCatsSubject.value.add(cat))

        api.addToFavourite(cat)
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<Cat> {
                override fun onNext(p0: Cat?) {
                    repository.addToFavourite(cat)
                }

                override fun onError(p0: Throwable?) {
                    favouriteCatsSubject.onNext(favouriteCatsSubject.value.remove(cat))
                }

                override fun onCompleted() {
                }

            })
    }

    fun removeFromFavourite(cat: Cat) {
        favouriteCatsSubject.onNext(favouriteCatsSubject.value.remove(cat))

        api.removeFromFavourite(cat)
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<Cat> {
                    override fun onNext(p0: Cat?) {
                        repository.removeFromFavourite(cat)
                    }

                    override fun onError(p0: Throwable?) {
                        favouriteCatsSubject.onNext(favouriteCatsSubject.value.add(cat))
                    }

                    override fun onCompleted() {
                    }

                })
    }


}
