package com.odai.firecats.cat.service

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.service.CatsService
import com.odai.firecats.event.Event
import io.reactivex.Flowable

class PersistedCatService(private val catsService: CatsService) : CatService {

    override fun getCatEvents(id: Int): Flowable<Event<Cat>> {
        return catsService.getCatsEvents().map {
            Event<Cat>(it.status, it?.data?.first { it.id == id }, it.error)
        }
    }

}
