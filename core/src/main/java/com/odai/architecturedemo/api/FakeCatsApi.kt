package com.odai.architecturedemo.api

import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.model.Cats
import rx.Observable
import rx.Scheduler
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class FakeCatsApi : CatApi {

    var favouriteCats = Cats(listOf(Cat(2, "Bar")))

    override fun getFavouriteCats(): Observable<Cats> {
        return Observable.just(favouriteCats).delay(2, TimeUnit.SECONDS, Schedulers.immediate())
    }

    override fun getCats(): Observable<Cats> {
        return Observable.just(Cats(listOf(Cat(1, "Foo"), Cat(2, "Bar")))).delay(2, TimeUnit.SECONDS, Schedulers.immediate())
    }

    override fun addToFavourite(cat: Cat): Observable<Cat> {
        return Observable.just(cat).delay(2, TimeUnit.SECONDS, Schedulers.immediate())
                .doOnNext {
                    favouriteCats = favouriteCats.add(cat)
                }
    }

    override fun removeFromFavourite(cat: Cat): Observable<Cat> {
        return Observable.just(cat).delay(2, TimeUnit.SECONDS, Schedulers.immediate())
                .doOnNext {
                    favouriteCats = favouriteCats.remove(cat)
                }
    }

}
