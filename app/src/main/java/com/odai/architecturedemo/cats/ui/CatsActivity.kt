package com.odai.architecturedemo.cats.ui

import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.odai.architecturedemo.CatApplication
import com.odai.architecturedemo.R
import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.api.FakeCatsApi
import com.odai.architecturedemo.cats.model.Cat
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.favourite.model.FavouriteCats
import com.odai.architecturedemo.favourite.model.FavouriteState
import com.odai.architecturedemo.event.Event
import com.odai.architecturedemo.event.Status
import rx.Notification
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription

class CatsActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null;
    var loadingView: TextView? = null;
    var subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadingView = findViewById(R.id.loadingView) as TextView
        recyclerView = findViewById(R.id.list) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = CatsAdapter(layoutInflater, listener, Cats(emptyList()), FavouriteCats(mapOf()))
        recyclerView!!.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
                outRect.bottom = resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
            }
        })
    }

    private fun getCatApplication(): CatApplication {
        return application as CatApplication
    }

    override fun onResume() {
        super.onResume()
        subscriptions.add(
                getCatApplication().catsUseCase.getCatsEvents()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(catsEventsObserver(loadingView!!, recyclerView!!))
        )
        subscriptions.add(
                getCatApplication().catsUseCase.getCats()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(catsObserver(recyclerView!!))
        )
        subscriptions.add(
                getCatApplication().favouriteCatsUseCase.getFavouriteCats()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(favouriteCatsObserver(recyclerView!!))
        )
    }

    override fun onPause() {
        super.onPause()
        subscriptions.clear()
        subscriptions = CompositeSubscription()
    }

    val listener: CatsActivity.CatClickedListener = object : CatsActivity.CatClickedListener {
        override fun onCatClicked(cat: Cat) {
            val catAdapter = recyclerView!!.adapter as CatsAdapter
            val favouriteState = catAdapter.favouriteCats.getStatusFor(cat)

            if (favouriteState == FavouriteState.FAVOURITE) {
                getCatApplication().favouriteCatsUseCase.removeFromFavourite(cat)
            } else if (favouriteState == FavouriteState.UN_FAVOURITE) {
                getCatApplication().favouriteCatsUseCase.addToFavourite(cat)
            }
        }
    }

    interface CatClickedListener {
        fun onCatClicked(cat: Cat): Unit
    }

    private fun catsEventsObserver(loadingView: TextView, recyclerView: RecyclerView): Observer<Event<Cats>> = object : Observer<Event<Cats>> {
        override fun onNext(p0: Event<Cats>) {
            when (p0.status) {
                Status.LOADING -> {
                    if (p0.data != null) {
                        loadingView.visibility = View.GONE
                        recyclerView.setBackgroundColor(recyclerView.resources.getColor(R.color.colorAccent, null))
                    } else {
                        loadingView.visibility = View.VISIBLE
                        loadingView.text = "LOADING"
                        recyclerView.setBackgroundColor(recyclerView.resources.getColor(android.R.color.transparent, null))
                    }
                }
                Status.IDLE -> {
                    if (p0.data != null) {
                        loadingView.visibility = View.GONE
                        recyclerView.setBackgroundColor(recyclerView.resources.getColor(android.R.color.transparent, null))
                    } else {
                        loadingView.visibility = View.VISIBLE
                        loadingView.text = "EMPTY"
                        recyclerView.setBackgroundColor(recyclerView.resources.getColor(android.R.color.transparent, null))
                    }
                }
                Status.ERROR -> Toast.makeText(loadingView.context, "An error has occurred: " + p0.error?.message , Toast.LENGTH_LONG).show()
            }
        }

        override fun onCompleted() {
            throw UnsupportedOperationException()
        }

        override fun onError(p0: Throwable?) {
            throw UnsupportedOperationException()
        }

    }

    private fun catsObserver(recyclerView: RecyclerView): Observer<Cats> = object : Observer<Cats> {
            override fun onNext(p0: Cats) {
                Log.d("Cats", "Got cats")
                var catAdapter = recyclerView.adapter as CatsAdapter
                catAdapter.cats = p0
                catAdapter.notifyDataSetChanged()
            }

            override fun onError(p0: Throwable?) {
                throw UnsupportedOperationException("Error on cats pipeline. This should never happen", p0)
            }

            override fun onCompleted() {
                throw UnsupportedOperationException("Completion on cats pipeline. This should never happen")
            }

        }

    private fun favouriteCatsObserver(recyclerView: RecyclerView): Observer<FavouriteCats> = object : Observer<FavouriteCats> {

            override fun onNext(p0: FavouriteCats) {
                Log.d("Cats", "Got new favourite cats")
                var catAdapter = recyclerView.adapter as CatsAdapter
                catAdapter.favouriteCats = p0
                catAdapter.notifyDataSetChanged()
            }

            override fun onError(p0: Throwable?) {
                throw UnsupportedOperationException("Error on favourite cats pipeline. This should never happen", p0)
            }

            override fun onCompleted() {
                throw UnsupportedOperationException("Completion on favourite cats pipeline. This should never happen")
            }

        }

}
