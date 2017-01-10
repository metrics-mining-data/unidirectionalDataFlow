package com.odai.firecats.cat

import com.odai.firecats.cat.displayer.CatDisplayer
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cat.service.CatService
import com.odai.firecats.event.DataObserver
import com.odai.firecats.event.Event
import com.odai.firecats.event.EventObserver
import com.odai.firecats.loading.LoadingDisplayer
import io.reactivex.disposables.CompositeDisposable

class CatPresenter(
        private val id: Int,
        private val catService: CatService,
        private val catDisplayer: CatDisplayer,
        private val loadingDisplayer: LoadingDisplayer
) {

    private var subscriptions = CompositeDisposable()

    fun startPresenting() {
        loadingDisplayer.attach(retryListener)
        subscriptions.add(
                catService.getCatEvents(id)
                        .subscribe(catEventsObserver)
        )
        subscriptions.add(
                catService.getCat(id)
                        .subscribe(catObserver)
        )
    }

    fun stopPresenting() {
        subscriptions.clear()
        subscriptions = CompositeDisposable()
    }

    private val catEventsObserver = object : EventObserver<Cat>() {
        override fun onLoading(event: Event<Cat>) {
            if (event.data != null) {
                loadingDisplayer.showLoadingIndicator()
            } else {
                loadingDisplayer.showLoadingScreen()
            }
        }

        override fun onIdle(event: Event<Cat>) {
            if (event.data != null) {
                loadingDisplayer.showData()
            } else {
                loadingDisplayer.showEmptyScreen()
            }
        }

        override fun onError(event: Event<Cat>) {
            if (event.data != null) {
                loadingDisplayer.showErrorIndicator()
            } else {
                loadingDisplayer.showErrorScreen()
            }
        }

    }

    private val catObserver = object : DataObserver<Cat> {
        override fun accept(p0: Cat) {
            catDisplayer.display(p0)
        }
    }

    val retryListener = object : LoadingDisplayer.LoadingActionListener {

        override fun onRetry() {
            catService.refreshCat()
        }

    }

}
