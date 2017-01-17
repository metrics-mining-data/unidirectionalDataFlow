package com.odai.firecats.cats

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.displayer.CatsDisplayer
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.cats.service.CatsService
import com.odai.firecats.event.DataObserver
import com.odai.firecats.event.Event
import com.odai.firecats.event.EventObserver
import com.odai.firecats.favourite.model.ActionState
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.favourite.model.FavouriteStatus
import com.odai.firecats.favourite.service.FavouriteCatsService
import com.odai.firecats.loading.LoadingDisplayer
import com.odai.firecats.navigation.Navigator
import io.reactivex.disposables.CompositeDisposable

class CatsPresenter(
        private val catsService: CatsService,
        private val favouriteCatsService: FavouriteCatsService,
        private val navigate: Navigator,
        private val catsDisplayer: CatsDisplayer,
        private val loadingDisplayer: LoadingDisplayer
) {

    private var subscriptions = CompositeDisposable()

    fun startPresenting() {
        catsDisplayer.attach(catClickedListener)
        loadingDisplayer.attach(retryListener)
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
                loadingDisplayer.showLoadingIndicator()
            } else {
                loadingDisplayer.showLoadingScreen()
            }
        }

        override fun onIdle(event: Event<Cats>) {
            if (event.data != null) {
                loadingDisplayer.showData()
            } else {
                loadingDisplayer.showEmptyScreen()
            }
        }

        override fun onError(event: Event<Cats>) {
            if (event.data != null) {
                loadingDisplayer.showErrorIndicator()
            } else {
                loadingDisplayer.showErrorScreen()
            }
        }

    }

    private val catsObserver = object : DataObserver<Cats> {
        override fun accept(p0: Cats) {
            catsDisplayer.display(p0)
        }
    }

    private val favouriteCatsObserver = object : DataObserver<FavouriteCats> {
        override fun accept(p0: FavouriteCats) {
            catsDisplayer.display(p0)
        }
    }

    val retryListener = object : LoadingDisplayer.LoadingActionListener {

        override fun onRetry() {
            //TBD if still needed
        }

    }

    val catClickedListener = object : CatsDisplayer.CatsActionListener {

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
