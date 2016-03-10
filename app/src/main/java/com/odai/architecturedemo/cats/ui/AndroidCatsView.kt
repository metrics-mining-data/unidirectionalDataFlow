package com.odai.architecturedemo.cats.ui

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.odai.architecturedemo.R
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.favourite.model.FavouriteCats

class AndroidCatsView(context: Context, attrs: AttributeSet): CatsView, FrameLayout(context, attrs) {

    private var _recyclerView: RecyclerView? = null
    private var _loadingView: TextView? = null

    private var recyclerView: RecyclerView
        get() = _recyclerView!!
        set(value) {
            _recyclerView = value
        };

    private var loadingView: TextView
        get() = _loadingView!!
        set(value) {
            _loadingView = value
        };

    override fun onFinishInflate() {
        super.onFinishInflate()
        loadingView = findViewById(R.id.loadingView) as TextView
        recyclerView = findViewById(R.id.list) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
                outRect.bottom = resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)
            }
        })
    }

    override fun attach(listener: CatsPresenter.CatClickedListener) {
        recyclerView.adapter = CatsAdapter(LayoutInflater.from(context), listener, Cats(emptyList()), FavouriteCats(mapOf()))
    }

    override fun display(cats: Cats) {
        var catAdapter = recyclerView.adapter as CatsAdapter
        catAdapter.cats = cats
        catAdapter.notifyDataSetChanged()
    }

    override fun display(favouriteCats: FavouriteCats) {
        var catAdapter = recyclerView.adapter as CatsAdapter
        catAdapter.favouriteCats = favouriteCats
        catAdapter.notifyDataSetChanged()
    }

    override fun showLoadingIndicator() {
        loadingView.visibility = View.GONE
        recyclerView.setBackgroundColor(recyclerView.resources.getColor(R.color.colorAccent, null))
    }

    override fun showLoadingScreen() {
        loadingView.visibility = View.VISIBLE
        loadingView.text = "LOADING"
        recyclerView.setBackgroundColor(recyclerView.resources.getColor(android.R.color.transparent, null))
    }

    override fun showData() {
        loadingView.visibility = View.GONE
        recyclerView.setBackgroundColor(recyclerView.resources.getColor(android.R.color.transparent, null))
    }

    override fun showEmptyScreen() {
        loadingView.visibility = View.VISIBLE
        loadingView.text = "EMPTY"
        recyclerView.setBackgroundColor(recyclerView.resources.getColor(android.R.color.transparent, null))
    }

    override fun showErrorIndicator() {
        Toast.makeText(loadingView.context, "An error has occurred", Toast.LENGTH_LONG).show()
    }

    override fun showErrorScreen() {
        loadingView.visibility = View.VISIBLE
        loadingView.text = "ERROR"
        recyclerView.setBackgroundColor(recyclerView.resources.getColor(android.R.color.transparent, null))
    }

}
