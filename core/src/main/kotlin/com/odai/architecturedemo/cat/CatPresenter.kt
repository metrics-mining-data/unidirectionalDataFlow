package com.odai.architecturedemo.cat

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cat.usecase.CatUseCase
import com.odai.architecturedemo.cat.view.CatView
import com.odai.architecturedemo.cats.CatsPresenter
import com.odai.architecturedemo.event.DataObserver
import com.odai.architecturedemo.event.Event
import com.odai.architecturedemo.event.EventObserver
import com.odai.architecturedemo.loading.LoadingView
import com.odai.architecturedemo.loading.RetryClickedListener
import rx.Observer
import rx.subscriptions.CompositeSubscription

class CatPresenter(
        val id: Int,
        val catUseCase: CatUseCase,
        val catView: CatView,
        val loadingView: LoadingView
) {

    var subscriptions = CompositeSubscription()

    fun startPresenting() {
        loadingView.attach(retryListener)
        subscriptions.add(
                catUseCase.getCatEvents(id)
                        .subscribe(catEventsObserver)
        )
        subscriptions.add(
                catUseCase.getCat(id)
                        .subscribe(catObserver)
        )
    }

    fun stopPresenting() {
        subscriptions.clear()
        subscriptions = CompositeSubscription()
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
        override fun onNext(p0: Cat) {
            catView.display(p0);
        }
    }

    val retryListener = object : RetryClickedListener {

        override fun onRetry() {
            catUseCase.refreshCat()
        }

    }

}
