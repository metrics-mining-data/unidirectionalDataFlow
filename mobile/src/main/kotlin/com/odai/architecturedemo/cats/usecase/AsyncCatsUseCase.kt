package com.odai.architecturedemo.cats.usecase

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class AsyncCatsUseCase(val catsUseCase: CatsUseCase) : CatsUseCase {

    override fun refreshCats() {
        Observable.create<Unit> { catsUseCase.refreshCats() }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    override fun getCatsEvents() = catsUseCase.getCatsEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getCats() = catsUseCase.getCats()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}
