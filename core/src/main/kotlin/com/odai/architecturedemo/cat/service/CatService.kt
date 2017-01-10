package com.odai.architecturedemo.cat.service

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.event.Event
import io.reactivex.Flowable

interface CatService {

    fun getCatEvents(id: Int): Flowable<Event<Cat>>

    fun getCat(id: Int): Flowable<Cat>

    fun refreshCat()

}
