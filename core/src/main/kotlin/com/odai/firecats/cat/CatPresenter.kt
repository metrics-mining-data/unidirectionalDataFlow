package com.odai.firecats.cat

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cat.service.CatService
import com.odai.firecats.cat.view.CatView
import com.odai.firecats.event.DataObserver
import com.odai.firecats.event.Event
import com.odai.firecats.event.EventObserver
import com.odai.firecats.loading.LoadingView
import com.odai.firecats.loading.RetryClickedListener
import io.reactivex.disposables.CompositeDisposable

class CatPresenter(
        private val id: Int,
        private val catService: CatService,
        private val catView: CatView,
        private val loadingView: LoadingView
) {

    private var subscriptions = CompositeDisposable()

    fun startPresenting() {
        loadingView.attach(retryListener)
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
                loadingView.showLoadingIndicator()
            } else {
                loadingView.showLoadingScreen()
            }
        }

        override fun onIdle(event: Event<Cat>) {
            if (event.data != null) {
                loadingView.showData()
            } else {
                loadingView.showEmptyScreen()
            }
        }

        override fun onError(event: Event<Cat>) {
            if (event.data != null) {
                loadingView.showErrorIndicator()
            } else {
                loadingView.showErrorScreen()
            }
        }

    }

    private val catObserver = object : DataObserver<Cat> {
        override fun accept(p0: Cat) {
            catView.display(p0)
        }
    }

    val retryListener = object : RetryClickedListener {

        override fun onRetry() {
            catService.refreshCat()
        }

    }

}
