package com.odai.firecats.cats.service

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.cats.model.CatsState
import com.odai.firecats.event.Event
import com.odai.firecats.favourite.model.ActionState
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.favourite.model.FavouriteStatus
import com.odai.firecats.favourite.service.FavouriteCatsService
import com.odai.firecats.login.model.User
import com.odai.firecats.login.service.LoginService
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

open class CatsServiceClient(
        private val loginService: LoginService,
        private val catsService: CatsService,
        private val favouriteService: FavouriteCatsService
) {

    private lateinit var user: User

    open public fun getCatsStateEvents(): Flowable<Event<CatsState>> {
        val favouriteCatsFlowable = loginService.getAuthentication()
                .filter { it.isSuccess }
                .doOnNext { user = it.user!! }
                .flatMap { auth -> favouriteService.getFavouriteCats(auth.user!!).map { CatsState(auth.user, null, it) } }
                .startWith(CatsState(null, null, FavouriteCats(emptyMap())))

        return Flowable.combineLatest(catsService.getCatsEvents(), favouriteCatsFlowable, BiFunction { event: Event<Cats>, state: CatsState ->
            Event(event.status, CatsState(state.user, event.data, state.favourites), event.error)
        })
    }

    open public fun toggleFavouriteStatus(cat: Cat, state: FavouriteState) {
        if (state.state != ActionState.CONFIRMED) {
            return
        }
        if (state.status == FavouriteStatus.FAVOURITE) {
            favouriteService.removeFromFavourite(user, cat)
        } else if (state.status == FavouriteStatus.UN_FAVOURITE) {
            favouriteService.addToFavourite(user, cat)
        }
    }

}
