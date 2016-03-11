package com.odai.architecturedemo.cats.usecase

import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.event.*
import com.odai.architecturedemo.persistence.CatRepository
import rx.Observable
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject

class PersistedCatsUseCase(val api: CatApi, val repository: CatRepository): CatsUseCase {

    val catsSubject: BehaviorSubject<Event<Cats>> = BehaviorSubject.create(Event<Cats>(Status.IDLE, null, null))

    override fun getCatsEvents(): Observable<Event<Cats>> {
        return catsSubject.asObservable()
                .doOnSubscribe { initialiseSubject() }
    }

    override fun getCats() = getCatsEvents().compose(asData())

    private fun initialiseSubject() {
        if (isInitialised(catsSubject)) {
            return
        }
        repository.readCats()
                .flatMap { updateFromRemoteIfOutdated(it) }
                .switchIfEmpty(fetchRemoteCats())
                .compose(asEvent<Cats>())
                .subscribeOn(Schedulers.immediate())
                .subscribe { catsSubject.onNext(it) }
    }

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
