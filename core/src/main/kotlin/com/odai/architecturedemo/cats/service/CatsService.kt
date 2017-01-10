package com.odai.architecturedemo.cats.service

import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.event.Event
import io.reactivex.Flowable


interface CatsService {

    fun getCatsEvents(): Flowable<Event<Cats>>

    fun getCats(): Flowable<Cats>

    fun refreshCats()

}
