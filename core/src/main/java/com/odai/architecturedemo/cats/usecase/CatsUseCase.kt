package com.odai.architecturedemo.cats.usecase

import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.event.*
import com.odai.architecturedemo.persistence.CatRepository
import rx.Observable
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject

class CatsUseCase(val api: CatApi, val repository: CatRepository) {

    val catsSubject: BehaviorSubject<Event<Cats>> = BehaviorSubject.create(Event<Cats>(Status.IDLE, null, null))

    fun getCatsEvents(): Observable<Event<Cats>> {
        if (isNotInitialised(catsSubject)) {
            repository.readCats()
                    .flatMap { updateFromRemoteIfOutdated(it) }
                    .switchIfEmpty(fetchRemoteCats())
                    .compose(asEvent<Cats>())
                    .subscribeOn(Schedulers.io())
                    .subscribe { catsSubject.onNext(it) }
        }
        return catsSubject.asObservable()
    }

    fun getCats() = getCatsEvents().compose(asData<Cats>())

    private fun updateFromRemoteIfOutdated(it: Cats): Observable<Cats> {
        return if (isOutdated(it)) {
            fetchRemoteCats().startWith(it)
        } else {
            Observable.just(it)
        }
    }

    private fun isOutdated(it: Cats): Boolean {
        return true;
    }

    private fun fetchRemoteCats() = api.getCats()
            .doOnNext { repository.saveCats(it) }

}
