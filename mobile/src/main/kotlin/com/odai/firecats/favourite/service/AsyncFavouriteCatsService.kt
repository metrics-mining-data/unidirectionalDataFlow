package com.odai.firecats.favourite.service

import com.odai.firecats.cat.model.Cat
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AsyncFavouriteCatsService(private val favouriteCatsService: FavouriteCatsService) : FavouriteCatsService {

    override fun getFavouriteCatsEvents() = favouriteCatsService.getFavouriteCatsEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getFavouriteCats() = favouriteCatsService.getFavouriteCats()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun addToFavourite(cat: Cat) {
        Completable.create { favouriteCatsService.addToFavourite(cat) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    override fun removeFromFavourite(cat: Cat) {
        Completable.create { favouriteCatsService.removeFromFavourite(cat) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

}
