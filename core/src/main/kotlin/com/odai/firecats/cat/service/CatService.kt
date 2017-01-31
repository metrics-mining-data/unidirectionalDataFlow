package com.odai.firecats.cat.service

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.event.Event
import io.reactivex.Flowable

interface CatService {

    fun getCatEvents(id: Int): Flowable<Event<Cat>>

}
