package com.odai.firecats.cats

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.cats.service.CatsService
import com.odai.firecats.cats.view.CatsView
import com.odai.firecats.event.DataObserver
import com.odai.firecats.event.Event
import com.odai.firecats.event.EventObserver
import com.odai.firecats.favourite.model.ActionState
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.favourite.model.FavouriteStatus
import com.odai.firecats.favourite.service.FavouriteCatsService
import com.odai.firecats.loading.LoadingView
import com.odai.firecats.loading.RetryClickedListener
import com.odai.firecats.navigation.Navigator
import io.reactivex.disposables.CompositeDisposable

class CatsPresenter(
        private val catsService: CatsService,
        private val favouriteCatsService: FavouriteCatsService,
        private val navigate: Navigator,
        private val catsView: CatsView,
        private val loadingView: LoadingView
) {

    private var subscriptions = CompositeDisposable()

    fun startPresenting() {
        catsView.attach(catClickedListener)
        loadingView.attach(retryListener)
        subscriptions.add(
                catsService.getCatsEvents()
                        .subscribe(catsEventsObserver)
        )
        subscriptions.add(
                catsService.getCats()
                        .subscribe(catsObserver)
        )
        subscriptions.add(
                favouriteCatsService.getFavouriteCats()
                        .subscribe(favouriteCatsObserver)
        )
    }

    fun stopPresenting() {
        subscriptions.clear()
        subscriptions = CompositeDisposable()
    }

    private val catsEventsObserver = object : EventObserver<Cats>() {
        override fun onLoading(event: Event<Cats>) {
            if (event.data != null) {
                loadingView.showLoadingIndicator()
            } else {
                loadingView.showLoadingScreen()
            }
        }

        override fun onIdle(event: Event<Cats>) {
            if (event.data != null) {
                loadingView.showData()
            } else {
                loadingView.showEmptyScreen()
            }
        }

        override fun onError(event: Event<Cats>) {
            if (event.data != null) {
                loadingView.showErrorIndicator()
            } else {
                loadingView.showErrorScreen()
            }
        }

    }

    private val catsObserver = object : DataObserver<Cats> {
        override fun accept(p0: Cats) {
            catsView.display(p0)
        }
    }

    private val favouriteCatsObserver = object : DataObserver<FavouriteCats> {
        override fun accept(p0: FavouriteCats) {
            catsView.display(p0)
        }
    }

    interface CatClickedListener {
        fun onFavouriteClicked(cat: Cat, state: FavouriteState)
        fun onCatClicked(cat: Cat)
    }

    val retryListener = object : RetryClickedListener {

        override fun onRetry() {
            catsService.refreshCats()
        }

    }

    val catClickedListener = object : CatClickedListener {

        override fun onCatClicked(cat: Cat) {
            navigate.toCat(cat)
        }

        override fun onFavouriteClicked(cat: Cat, state: FavouriteState) {
            if(state.state != ActionState.CONFIRMED) {
                return
            }
            if (state.status == FavouriteStatus.FAVOURITE) {
                favouriteCatsService.removeFromFavourite(cat)
            } else if (state.status == FavouriteStatus.UN_FAVOURITE) {
                favouriteCatsService.addToFavourite(cat)
            }
        }

    }

}
