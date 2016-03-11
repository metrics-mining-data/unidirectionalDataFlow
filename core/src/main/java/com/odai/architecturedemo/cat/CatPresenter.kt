package com.odai.architecturedemo.cat

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cat.usecase.CatUseCase
import com.odai.architecturedemo.cat.view.CatView
import com.odai.architecturedemo.event.Event
import com.odai.architecturedemo.event.EventObserver
import rx.Observer
import rx.subscriptions.CompositeSubscription

class CatPresenter(
        val id: Int,
        val catUseCase: CatUseCase,
        val catView: CatView
) {

    var subscriptions = CompositeSubscription()

    fun startPresenting() {
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

    private val catEventsObserver: Observer<Event<Cat>>
        get() = object : EventObserver<Cat>() {
            override fun onLoading(event: Event<Cat>) {
                if (event.data != null) {
                    catView.showLoadingIndicator()
                } else {
                    catView.showLoadingScreen()
                }
            }

            override fun onIdle(event: Event<Cat>) {
                if (event.data != null) {
                    catView.showData()
                } else {
                    catView.showEmptyScreen()
                }
            }

            override fun onError(event: Event<Cat>) {
                if (event.data != null) {
                    catView.showErrorScreen()
                } else {
                    catView.showErrorIndicator()
                }
            }

        }

    private val catObserver: Observer<Cat>
        get() = object : Observer<Cat> {
            override fun onNext(p0: Cat) {
                catView.display(p0);
            }

            override fun onError(p0: Throwable?) {
                throw UnsupportedOperationException("Error on cats pipeline. This should never happen", p0)
            }

            override fun onCompleted() {
                throw UnsupportedOperationException("Completion on cats pipeline. This should never happen")
            }
        }

}
