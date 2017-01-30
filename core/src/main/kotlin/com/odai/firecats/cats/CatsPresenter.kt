package com.odai.firecats.cats

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.displayer.CatsDisplayer
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.cats.service.CatsService
import com.odai.firecats.event.Event
import com.odai.firecats.event.EventObserver
import com.odai.firecats.favourite.model.ActionState
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.favourite.model.FavouriteStatus
import com.odai.firecats.favourite.service.FavouriteCatsService
import com.odai.firecats.login.model.User
import com.odai.firecats.login.service.LoginService
import com.odai.firecats.navigation.Navigator
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

class CatsPresenter(
        private val loginService: LoginService,
        private val catsService: CatsService,
        private val favouriteCatsService: FavouriteCatsService,
        private val navigate: Navigator,
        private val catsDisplayer: CatsDisplayer
) {

    private var subscriptions = CompositeDisposable()
    private lateinit var user: User

    fun startPresenting() {
        catsDisplayer.attach(catClickedListener)
        val favouriteCatsFlowable = loginService.getAuthentication()
                .filter { it.isSuccess }
                .doOnNext { user = it.user!! }
                .flatMap { favouriteCatsService.getFavouriteCats(it.user!!) }
                .startWith(FavouriteCats(emptyMap()))

        val eventFlow = Flowable.combineLatest(catsService.getCatsEvents(), favouriteCatsFlowable, BiFunction { event:Event<Cats>, fav: FavouriteCats ->
            Event(event.status, Pair(event.data, fav), event.error)
        })

        subscriptions.add(
                eventFlow.subscribe(catsEventsObserver)
        )
    }

    fun stopPresenting() {
        catsDisplayer.detach(catClickedListener)
        subscriptions.clear()
        subscriptions = CompositeDisposable()
    }

    private val catsEventsObserver = object : EventObserver<Pair<Cats?, FavouriteCats>>() {
        override fun onLoading(event: Event<Pair<Cats?, FavouriteCats>>) {
            if (event.data?.first != null) {
                catsDisplayer.displayLoading(event.data?.first!!, event.data?.second!!)
            } else {
                catsDisplayer.displayLoading()
            }
        }

        override fun onIdle(event: Event<Pair<Cats?, FavouriteCats>>) {
            if (event.data?.first != null) {
                catsDisplayer.display(event.data?.first!!, event.data?.second!!)
            } else {
                catsDisplayer.displayEmpty()
            }
        }

        override fun onError(event: Event<Pair<Cats?, FavouriteCats>>) {
            if (event.data?.first != null) {
                catsDisplayer.displayError(event.data?.first!!, event.data?.second!!)
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
            if (state.state != ActionState.CONFIRMED) {
                return
            }
            if (state.status == FavouriteStatus.FAVOURITE) {
                favouriteCatsService.removeFromFavourite(user, cat)
            } else if (state.status == FavouriteStatus.UN_FAVOURITE) {
                favouriteCatsService.addToFavourite(user, cat)
            }
        }

        override fun onRetry() {
            //TBD if still needed
        }

    }

}
