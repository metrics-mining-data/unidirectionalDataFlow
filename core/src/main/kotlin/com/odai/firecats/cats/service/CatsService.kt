package com.odai.firecats.cats.service

import com.odai.firecats.cats.model.Cats
import com.odai.firecats.event.Event
import io.reactivex.Flowable


interface CatsService {

    fun getCatsEvents(): Flowable<Event<Cats>>

    fun getCats(): Flowable<Cats>

}
