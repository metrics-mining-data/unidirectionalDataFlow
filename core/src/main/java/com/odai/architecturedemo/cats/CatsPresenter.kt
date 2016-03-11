package com.odai.architecturedemo.cats

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.cats.usecase.CatsUseCase
import com.odai.architecturedemo.cats.view.CatsView
import com.odai.architecturedemo.event.Event
import com.odai.architecturedemo.event.EventObserver
import com.odai.architecturedemo.favourite.model.FavouriteCats
import com.odai.architecturedemo.favourite.model.FavouriteState
import com.odai.architecturedemo.favourite.usecase.FavouriteCatsUseCase
import com.odai.architecturedemo.navigation.Navigator
import rx.Observer
import rx.subscriptions.CompositeSubscription

class CatsPresenter(
        val catsUseCase: CatsUseCase,
        val favouriteCatsUseCase: FavouriteCatsUseCase,
        val navigate: Navigator,
        val catsView: CatsView
) {

    var subscriptions = CompositeSubscription()

    fun startPresenting() {
        catsView.attach(listener)
        subscriptions.add(
                catsUseCase.getCatsEvents()
                        .subscribe(catsEventsObserver)
        )
        subscriptions.add(
                catsUseCase.getCats()
                        .subscribe(catsObserver)
        )
        subscriptions.add(
                favouriteCatsUseCase.getFavouriteCats()
                        .subscribe(favouriteCatsObserver)
        )
    }

    fun stopPresenting() {
        subscriptions.clear()
        subscriptions = CompositeSubscription()
    }

    private val catsEventsObserver: Observer<Event<Cats>>
        get() = object : EventObserver<Cats>() {
            override fun onLoading(event: Event<Cats>) {
                if (event.data != null) {
                    catsView.showLoadingIndicator()
                } else {
                    catsView.showLoadingScreen()
                }
            }

            override fun onIdle(event: Event<Cats>) {
                if (event.data != null) {
                    catsView.showData()
                } else {
                    catsView.showEmptyScreen()
                }
            }

            override fun onError(event: Event<Cats>) {
                if (event.data != null) {
                    catsView.showErrorScreen()
                } else {
                    catsView.showErrorIndicator()
                }
            }

        }

    private val catsObserver: Observer<Cats>
        get() = object : Observer<Cats> {
            override fun onNext(p0: Cats) {
                catsView.display(p0);
            }

            override fun onError(p0: Throwable?) {
                throw UnsupportedOperationException("Error on cats pipeline. This should never happen", p0)
            }

            override fun onCompleted() {
                throw UnsupportedOperationException("Completion on cats pipeline. This should never happen")
            }
        }

    private val favouriteCatsObserver: Observer<FavouriteCats>
        get() = object : Observer<FavouriteCats> {
            override fun onNext(p0: FavouriteCats) {
                catsView.display(p0)
            }

            override fun onError(p0: Throwable?) {
                throw UnsupportedOperationException("Error on favourite cats pipeline. This should never happen", p0)
            }

            override fun onCompleted() {
                throw UnsupportedOperationException("Completion on favourite cats pipeline. This should never happen")
            }
        }

    interface CatClickedListener {
        fun onFavouriteClicked(cat: Cat, state: FavouriteState)
        fun onCatClicked(cat: Cat)
    }

    val listener: CatClickedListener = object : CatClickedListener {

        override fun onCatClicked(cat: Cat) {
            navigate.toCat(cat)
        }

        override fun onFavouriteClicked(cat: Cat, state: FavouriteState) {
            if (state == FavouriteState.FAVOURITE) {
                favouriteCatsUseCase.removeFromFavourite(cat)
            } else if (state == FavouriteState.UN_FAVOURITE) {
                favouriteCatsUseCase.addToFavourite(cat)
            }
        }

    }

}
