package com.odai.architecturedemo.cat.usecase

import com.odai.architecturedemo.cat.usecase.CatUseCase
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class AsyncCatUseCase(val catUseCase: CatUseCase) : CatUseCase {

    override fun getCatEvents(id: Int) = catUseCase.getCatEvents(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getCat(id: Int) = catUseCase.getCat(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}
