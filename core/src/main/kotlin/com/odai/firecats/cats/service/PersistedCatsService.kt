package com.odai.firecats.cats.service

import com.jakewharton.rxrelay2.BehaviorRelay
import com.odai.firecats.api.CatApi
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.event.*
import com.odai.firecats.persistence.CatRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class PersistedCatsService(
        private val api: CatApi,
        private val repository: CatRepository,
        private val catsFreshnessChecker: CatsFreshnessChecker
) : CatsService {

    private val catsRelay: BehaviorRelay<Event<Cats>> = BehaviorRelay.createDefault(Event<Cats>(Status.IDLE, null, null))

    override fun getCatsEvents(): Flowable<Event<Cats>> {
        return catsRelay.toFlowable(BackpressureStrategy.LATEST)
                .startWith(initialiseSubject())
                .distinctUntilChanged()
    }

    override fun getCats(): Flowable<Cats> = getCatsEvents().compose(asData())

    override fun refreshCats() {
        fetchRemoteCats()
                .compose(asEvent<Cats>())
                .subscribe { catsRelay.accept(it) }
    }

    private fun initialiseSubject(): Flowable<Event<Cats>> {
        if (isInitialised(catsRelay)) {
            return Flowable.empty()
        }
        return repository.readCats()
                .flatMap { updateFromRemoteIfOutdated(it) }
                .switchIfEmpty(fetchRemoteCats())
                .compose(asEvent<Cats>())
                .doOnNext { catsRelay.accept(it) }
    }

    private fun updateFromRemoteIfOutdated(it: Cats): Flowable<Cats> {
        return if (catsFreshnessChecker.isFresh(it)) {
            Flowable.just(it)
        } else {
            fetchRemoteCats().startWith(it)
        }
    }

    private fun fetchRemoteCats() = api.getCats()
            .doOnNext { repository.saveCats(it) }

}
