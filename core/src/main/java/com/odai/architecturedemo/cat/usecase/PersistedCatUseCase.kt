package com.odai.architecturedemo.cat.usecase

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.usecase.CatsUseCase
import com.odai.architecturedemo.event.Event
import com.odai.architecturedemo.event.asData
import rx.Observable

class PersistedCatUseCase(val catsUseCase: CatsUseCase) : CatUseCase {

    override fun getCatEvents(id: Int): Observable<Event<Cat>> {
        return catsUseCase.getCatsEvents().map {
            Event<Cat>(it.status, it?.data?.list?.first { it.id == id }, it.error)
        }
    }

    override fun getCat(id: Int): Observable<Cat> {
        return getCatEvents(id).compose(asData())
    }

}
