package com.odai.architecturedemo.ui

import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.odai.architecturedemo.CatApplication
import com.odai.architecturedemo.model.Cats
import com.odai.architecturedemo.R
import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.api.FakeCatsApi
import com.odai.architecturedemo.model.Cat
import com.odai.architecturedemo.model.FavouriteCats
import com.odai.architecturedemo.model.FavouriteState
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription

class MainActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null;
    var subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.list) as RecyclerView?
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
