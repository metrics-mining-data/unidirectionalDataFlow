package com.odai.firecats.cats.service

import com.odai.firecats.cats.model.Cats
import com.odai.firecats.event.Event
import com.odai.firecats.event.asData
import com.odai.firecats.event.asEvent
import com.odai.firecats.persistence.CatRepository
import io.reactivex.Flowable

class PersistedCatsService(
        private val repository: CatRepository
) : CatsService {

    override fun getCatsEvents(): Flowable<Event<Cats>> {
        return repository.observeCats()
                .compose(asEvent())
                .map { enforceNonEmpty(it) }
    }

    override fun getCats(): Flowable<Cats> = getCatsEvents().compose(asData())

    private fun enforceNonEmpty(event: Event<Cats>): Event<Cats> {
        if (event.data?.isEmpty() ?: false) {
            return Event(event.status, null, event.error)
        } else {
            return event
        }
    }

}
