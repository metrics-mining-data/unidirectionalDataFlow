package com.odai.architecturedemo.cat.usecase

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.event.Event
import rx.Observable

interface CatUseCase {

    fun getCatEvents(id: Int): Observable<Event<Cat>>

    fun getCat(id: Int): Observable<Cat>

}
