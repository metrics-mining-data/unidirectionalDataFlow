package com.odai.architecturedemo.cats.usecase

import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.event.Event
import rx.Observable


interface CatsUseCase {

    fun getCatsEvents(): Observable<Event<Cats>>

    fun getCats(): Observable<Cats>

}
