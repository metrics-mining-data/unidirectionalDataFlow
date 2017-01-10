package com.odai.firecats.event

import io.reactivex.functions.Consumer

abstract class EventObserver<T>: Consumer<Event<T>> {

    override fun accept(p0: Event<T>) {
        when (p0.status) {
            Status.LOADING -> onLoading(p0)
            Status.IDLE -> onIdle(p0)
            Status.ERROR -> onError(p0)
        }
    }

    abstract fun onLoading(event: Event<T>);

    abstract fun onIdle(event: Event<T>);

    abstract fun onError(event: Event<T>);
}
