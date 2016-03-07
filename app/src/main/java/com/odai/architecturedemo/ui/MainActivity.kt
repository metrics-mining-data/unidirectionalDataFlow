package com.odai.architecturedemo.ui

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
import com.odai.architecturedemo.model.Cats
import com.odai.architecturedemo.R
import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.api.FakeCatsApi
import com.odai.architecturedemo.event.Event
import com.odai.architecturedemo.event.Status
import com.odai.architecturedemo.model.Cat
import com.odai.architecturedemo.model.FavouriteCats
import com.odai.architecturedemo.model.FavouriteState
import rx.Notification
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription

class MainActivity : AppCompatActivity() {

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
                outRect.top = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin)
                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin)
            }
        })
    }

    private fun getCatApplication(): CatApplication {
        return application as CatApplication
    }

    override fun onResume() {
        super.onResume()
        subscriptions.add(
                getCatApplication().catUseCase.getCatsEvents()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(CatsEventsObserver(loadingView!!, recyclerView!!))
        )
        subscriptions.add(
                getCatApplication().catUseCase.getCats()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(CatsObserver(layoutInflater, recyclerView!!, listener))
        )
        subscriptions.add(
                getCatApplication().catUseCase.getFavouriteCats()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(FavouriteCatsObserver(recyclerView!!))
        )
    }

    override fun onPause() {
        super.onPause()
        subscriptions.clear()
        subscriptions = CompositeSubscription()
    }

    val listener: CatClickedListener = object : CatClickedListener {
        override fun onCatClicked(cat: Cat) {
            val catAdapter = recyclerView!!.adapter as CatsAdapter
            val favouriteState = catAdapter.favouriteCats.getStatusFor(cat)
            if (favouriteState == FavouriteState.FAVOURITE) {
                getCatApplication().catUseCase.removeFromFavourite(cat)
            } else if (favouriteState == FavouriteState.UN_FAVOURITE) {
                getCatApplication().catUseCase.addToFavourite(cat)
            }
        }
    }

    interface CatClickedListener {
        fun onCatClicked(cat: Cat): Unit
    }

    class CatsEventsObserver(val loadingView: TextView, val recyclerView: RecyclerView) : Observer<Event<Cats>> {

        override fun onNext(p0: Event<Cats>) {
            when(p0.status) {
                Status.LOADING -> {
                    if(p0.data != null) {
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
                Status.ERROR -> Toast.makeText(loadingView.context, "An error has occured: " + p0.error?.message ?: "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }

        override fun onCompleted() {
            throw UnsupportedOperationException()
        }

        override fun onError(p0: Throwable?) {
            throw UnsupportedOperationException()
        }

    }

    class CatsObserver(
            val layoutInflater: LayoutInflater,
            val recyclerView: RecyclerView,
            val listener: CatClickedListener
    ) : Observer<Cats> {

        override fun onNext(p0: Cats) {
            Log.d("Cats", "Got cats")
            var catAdapter = recyclerView.adapter as CatsAdapter
            catAdapter.cats = p0
            catAdapter.notifyDataSetChanged()
        }

        override fun onError(p0: Throwable?) {
            Log.e("Cats", "Failed to load cats", p0)
        }

        override fun onCompleted() {
            Log.d("Cats", "Cats Completed")
        }

    }

    class FavouriteCatsObserver(val recyclerView: RecyclerView) : Observer<FavouriteCats> {

        override fun onNext(p0: FavouriteCats) {
            Log.d("Cats", "Got new favourite cats")
            var catAdapter = recyclerView.adapter as CatsAdapter
            catAdapter.favouriteCats = p0
            catAdapter.notifyDataSetChanged()
        }

        override fun onError(p0: Throwable?) {
            Log.e("Favourite Cats", "Failed to load Favourite Cats", p0)
        }

        override fun onCompleted() {
            Log.d("Favourite Cats", "Favourite Cats Completed")
        }
    }
}
