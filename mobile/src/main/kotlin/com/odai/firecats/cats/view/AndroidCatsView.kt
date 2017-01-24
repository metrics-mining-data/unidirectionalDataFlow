package com.odai.firecats.cats.view

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.odai.firecats.R
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.displayer.CatsDisplayer
import com.odai.firecats.cats.model.Cats
import com.odai.firecats.favourite.model.FavouriteCats
import com.odai.firecats.favourite.model.FavouriteState

class AndroidCatsView(context: Context, attrs: AttributeSet): CatsDisplayer, RecyclerView(context, attrs) {

    private var listener: CatsDisplayer.CatsActionListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
                outRect.top = resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
                outRect.bottom = resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
            }
        })
        adapter = CatsAdapter(LayoutInflater.from(context), delegateListener, Cats(emptyList()), FavouriteCats(mapOf()))
    }

    override fun attach(listener: CatsDisplayer.CatsActionListener) {
        this.listener = listener
    }

    override fun detach(listener: CatsDisplayer.CatsActionListener) {
        this.listener = null
    }

    override fun display(cats: Cats) {
        val catAdapter = adapter as CatsAdapter
        catAdapter.cats = cats
        catAdapter.notifyDataSetChanged()
    }

    override fun display(favouriteCats: FavouriteCats) {
        val catAdapter = adapter as CatsAdapter
        catAdapter.favouriteCats = favouriteCats
        catAdapter.notifyDataSetChanged()
    }

    val delegateListener = object: Listener {
        override fun onFavouriteClicked(cat: Cat, state: FavouriteState) {
            listener!!.onFavouriteClicked(cat, state);
        }

        override fun onCatClicked(cat: Cat) {
            listener!!.onCatClicked(cat);
        }

    }

    interface Listener {

        fun onFavouriteClicked(cat: Cat, state: FavouriteState)

        fun onCatClicked(cat: Cat)

    }

}
