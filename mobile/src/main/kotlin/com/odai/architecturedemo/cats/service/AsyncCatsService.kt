package com.odai.architecturedemo.cats.service

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AsyncCatsService(private val catsService: CatsService) : CatsService {

    override fun refreshCats() {
        Completable.create { catsService.refreshCats() }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    override fun getCatsEvents() = catsService.getCatsEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getCats() = catsService.getCats()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}
