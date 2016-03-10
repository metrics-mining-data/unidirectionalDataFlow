package com.odai.architecturedemo.event

import rx.Notification
import rx.Observable
import rx.subjects.BehaviorSubject

fun <T> asEvent() = Observable.Transformer<T, Event<T>> { p0 ->
    p0.materialize()
            .scan(Event<T>(Status.LOADING, null, null)) { event, notification ->
                when (notification.kind) {
                    Notification.Kind.OnNext -> Event(Status.LOADING, notification.value, null)
                    Notification.Kind.OnCompleted -> Event(Status.IDLE, event.data, null)
                    Notification.Kind.OnError -> Event(Status.ERROR, event.data, notification.throwable)
                    null -> event
                }
            }.startWith(Event<T>(Status.LOADING, null, null))
}

fun <T> asData() = Observable.Transformer<Event<T>, T> { p0 ->
    p0.filter { it.data != null }
            .map { it.data }
            .distinctUntilChanged()
}

fun <T> isNotInitialised(subject: BehaviorSubject<Event<T>>) = subject.value.status != Status.LOADING && subject.value.data == null
