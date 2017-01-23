package com.odai.firecats.event

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.FlowableTransformer
import io.reactivex.Notification

fun <T> asEvent() = FlowableTransformer<T, Event<T>> { p0 ->
    p0.materialize()
            .map { Pair<Event<T>?, Notification<T>?>(null, it) }
            .startWith(Pair<Event<T>?, Notification<T>?>(Event<T>(Status.LOADING, null, null), null))
            .scan { acc, pair ->
                if (acc.first == null || acc.second != null || pair.second == null) {
                    throw IllegalStateException("This hack went wrong")
                } else {
                    return@scan Pair<Event<T>?, Notification<T>?>(foo(acc.first!!, pair.second!!), null)
                }
            }.map { it.first }
            .distinctUntilChanged()
}

private fun <T> foo(event: Event<T>, notification: Notification<T>): Event<T>? {
    return when {
        notification.isOnNext -> Event(Status.IDLE, notification.value, null)
        notification.isOnComplete -> Event(Status.ERROR, event.data, Exception("Unexpected end of stream"))
        notification.isOnError -> Event(Status.ERROR, event.data, notification.error)
        else -> event
    }
}

fun <T> asData() = FlowableTransformer<Event<T>, T> { p0 ->
    p0.filter { it.data != null }
            .map { it.data }
            .distinctUntilChanged()
}

fun <T> isInitialised(subject: BehaviorRelay<Event<T>>) = subject.value.data != null || subject.value.status == Status.LOADING
