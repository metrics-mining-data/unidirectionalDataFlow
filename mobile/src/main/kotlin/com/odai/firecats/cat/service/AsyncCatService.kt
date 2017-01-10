package com.odai.firecats.cat.service

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AsyncCatService(private val catService: CatService) : CatService {

    override fun refreshCat() {
        Completable.create { catService.refreshCat() }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    override fun getCatEvents(id: Int) = catService.getCatEvents(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getCat(id: Int) = catService.getCat(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}
