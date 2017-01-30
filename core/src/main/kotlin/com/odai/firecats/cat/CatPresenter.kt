package com.odai.firecats.cat

import com.odai.firecats.cat.displayer.CatDisplayer
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cat.service.CatService
import com.odai.firecats.event.Event
import com.odai.firecats.event.EventObserver
import io.reactivex.disposables.CompositeDisposable

class CatPresenter(
        private val id: Int,
        private val catService: CatService,
        private val catDisplayer: CatDisplayer
) {

    private var subscriptions = CompositeDisposable()

    fun startPresenting() {
        subscriptions.add(
                catService.getCatEvents(id)
                        .subscribe(catEventsObserver)
        )
    }

    fun stopPresenting() {
        subscriptions.clear()
        subscriptions = CompositeDisposable()
    }

    private val catEventsObserver = object : EventObserver<Cat>() {
        override fun onLoading(event: Event<Cat>) {
            if (event.data != null) {
                catDisplayer.displayLoading(event.data)
            } else {
                catDisplayer.displayLoading()
            }
        }

        override fun onIdle(event: Event<Cat>) {
            if (event.data != null) {
                catDisplayer.display(event.data)
            } else {
                catDisplayer.displayEmpty()
            }
        }

        override fun onError(event: Event<Cat>) {
            if (event.data != null) {
                catDisplayer.displayError(event.data)
            } else {
                catDisplayer.displayError()
            }
        }
    }

}
