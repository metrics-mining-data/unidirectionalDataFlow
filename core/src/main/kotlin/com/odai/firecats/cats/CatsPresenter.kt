package com.odai.firecats.cats

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.displayer.CatsDisplayer
import com.odai.firecats.cats.model.CatsState
import com.odai.firecats.cats.service.CatsServiceClient
import com.odai.firecats.event.Event
import com.odai.firecats.event.EventObserver
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.navigation.Navigator
import io.reactivex.disposables.CompositeDisposable

class CatsPresenter(
        private val catsServiceClient: CatsServiceClient,
        private val navigate: Navigator,
        private val catsDisplayer: CatsDisplayer
) {

    private var subscriptions = CompositeDisposable()

    fun startPresenting() {
        catsDisplayer.attach(catClickedListener)
        subscriptions.add(
                catsServiceClient.getCatsStateEvents().subscribe(catsEventsObserver)
        )
    }

    fun stopPresenting() {
        catsDisplayer.detach(catClickedListener)
        subscriptions.clear()
        subscriptions = CompositeDisposable()
    }

    private val catsEventsObserver = object : EventObserver<CatsState>() {
        override fun onLoading(event: Event<CatsState>) {
            if (event.data?.cats != null) {
                catsDisplayer.displayLoading(event.data?.cats, event.data?.favourites!!)
            } else {
                catsDisplayer.displayLoading()
            }
        }

        override fun onIdle(event: Event<CatsState>) {
            if (event.data?.cats != null) {
                catsDisplayer.display(event.data?.cats, event.data?.favourites!!)
            } else {
                catsDisplayer.displayEmpty()
            }
        }

        override fun onError(event: Event<CatsState>) {
            if (event.data?.cats != null) {
                catsDisplayer.displayError(event.data?.cats, event.data?.favourites!!)
            } else {
                catsDisplayer.displayError()
            }
        }
    }

    val catClickedListener = object : CatsDisplayer.CatsActionListener {

        override fun onCatClicked(cat: Cat) {
            navigate.toCat(cat)
        }

        override fun onFavouriteClicked(cat: Cat, state: FavouriteState) {
            catsServiceClient.toggleFavouriteStatus(cat, state)
        }

        override fun onRetry() {
            //TBD if still needed
        }

    }

}
