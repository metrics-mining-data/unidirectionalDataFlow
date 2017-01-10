package com.odai.firecats.event

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.FlowableTransformer

fun <T> asEvent() = FlowableTransformer<T, Event<T>> { p0 ->
    p0.materialize()
            .scan(Event<T>(Status.LOADING, null, null)) { event, notification ->
                when {
                    notification.isOnNext -> Event(Status.LOADING, notification.value, null)
                    notification.isOnComplete -> Event(Status.IDLE, event.data, null)
                    notification.isOnError -> Event(Status.ERROR, event.data, notification.error)
                    else -> event
                }
            }.startWith(Event<T>(Status.LOADING, null, null))
}

fun <T> asData() = FlowableTransformer<Event<T>, T> { p0 ->
    p0.filter { it.data != null }
            .map { it.data }
            .distinctUntilChanged()
}

fun <T> isInitialised(subject: BehaviorRelay<Event<T>>) = subject.value.data != null || subject.value.status == Status.LOADING
