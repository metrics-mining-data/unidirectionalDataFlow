package com.odai.firecats.event

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

fun <T> asEvent() = FlowableTransformer<T, Event<T>> { p0 ->
    p0.materialize()
            .flatMap {
                Flowable.just(it, it)
            }
            .scan(Event<T>(Status.LOADING, null, null)) { event, notification ->
                when {
                    notification.isOnNext -> return@scan Event(Status.IDLE, notification.value, null)
                    notification.isOnComplete -> return@scan Event(Status.ERROR, event.data, Exception("Unexpected end of stream"))
                    notification.isOnError -> return@scan Event(Status.ERROR, event.data, notification.error)
                    else -> return@scan event
                }
            }.distinctUntilChanged()
}

fun <T> asData() = FlowableTransformer<Event<T>, T> { p0 ->
    p0.filter { it.data != null }
            .map { it.data }
            .distinctUntilChanged()
}

fun <T> isInitialised(subject: BehaviorRelay<Event<T>>) = subject.value.data != null || subject.value.status == Status.LOADING
