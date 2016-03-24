package com.odai.architecturedemo.favourite.usecase

import com.odai.architecturedemo.cat.model.Cat
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class AsyncFavouriteCatsUseCase(val favouriteCatsUseCase: FavouriteCatsUseCase) : FavouriteCatsUseCase {

    override fun getFavouriteCatsEvents() = favouriteCatsUseCase.getFavouriteCatsEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getFavouriteCats() = favouriteCatsUseCase.getFavouriteCats()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun addToFavourite(cat: Cat) {
        Observable.create<Unit> { favouriteCatsUseCase.addToFavourite(cat) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    override fun removeFromFavourite(cat: Cat) {
        Observable.create<Unit> { favouriteCatsUseCase.removeFromFavourite(cat) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

}
