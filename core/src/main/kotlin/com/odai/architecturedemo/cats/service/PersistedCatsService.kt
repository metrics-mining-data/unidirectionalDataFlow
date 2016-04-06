package com.odai.architecturedemo.cats.service

import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.event.*
import com.odai.architecturedemo.persistence.CatRepository
import rx.Observable
import rx.subjects.BehaviorSubject

class PersistedCatsService(
        private val api: CatApi,
        private val repository: CatRepository,
        private val catsFreshnessChecker: CatsFreshnessChecker
) : CatsService {

    private val catsSubject: BehaviorSubject<Event<Cats>> = BehaviorSubject.create(Event<Cats>(Status.IDLE, null, null))

    override fun getCatsEvents(): Observable<Event<Cats>> {
        return catsSubject.asObservable()
                .startWith(initialiseSubject())
                .distinctUntilChanged()
    }

    override fun getCats() = getCatsEvents().compose(asData())

    override fun refreshCats() {
        fetchRemoteCats()
                .compose(asEvent<Cats>())
                .subscribe { catsSubject.onNext(it) }
    }

    private fun initialiseSubject(): Observable<Event<Cats>> {
        if (isInitialised(catsSubject)) {
            return Observable.empty()
        }
        return repository.readCats()
                .flatMap { updateFromRemoteIfOutdated(it) }
                .switchIfEmpty(fetchRemoteCats())
                .compose(asEvent<Cats>())
                .doOnNext { catsSubject.onNext(it) }
    }

    private fun updateFromRemoteIfOutdated(it: Cats): Observable<Cats> {
        return if (catsFreshnessChecker.isFresh(it)) {
            Observable.just(it)
        } else {
            fetchRemoteCats().startWith(it)
        }
    }

    private fun fetchRemoteCats() = api.getCats()
            .doOnNext { repository.saveCats(it) }

}
