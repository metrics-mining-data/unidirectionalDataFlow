package com.odai.architecturedemo.cat.view

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
import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.model.Cats
import com.odai.architecturedemo.cats.CatsPresenter
import com.odai.architecturedemo.cats.view.CatsAdapter
import com.odai.architecturedemo.cats.view.CatsView
import com.odai.architecturedemo.favourite.model.FavouriteCats

class AndroidCatView(context: Context, attrs: AttributeSet): CatView, FrameLayout(context, attrs) {

    private var _labelView: TextView? = null
    private var _loadingView: TextView? = null

    private var labelView: TextView
        get() = _labelView!!
        set(value) {
            _labelView = value
        };

    private var loadingView: TextView
        get() = _loadingView!!
        set(value) {
            _loadingView = value
        };

    override fun onFinishInflate() {
        super.onFinishInflate()
        loadingView = findViewById(R.id.loadingView) as TextView
        labelView = findViewById(R.id.catLabel) as TextView
    }

    override fun display(cat: Cat) {
        labelView.text = cat.name
    }

    override fun showLoadingIndicator() {
        loadingView.visibility = View.GONE
        labelView.setBackgroundColor(labelView.resources.getColor(R.color.colorAccent, null))
    }

    override fun showLoadingScreen() {
        loadingView.visibility = View.VISIBLE
        loadingView.text = "LOADING"
        labelView.setBackgroundColor(labelView.resources.getColor(android.R.color.transparent, null))
    }

    override fun showData() {
        loadingView.visibility = View.GONE
        labelView.setBackgroundColor(labelView.resources.getColor(android.R.color.transparent, null))
    }

    override fun showEmptyScreen() {
        loadingView.visibility = View.VISIBLE
        loadingView.text = "EMPTY"
        labelView.setBackgroundColor(labelView.resources.getColor(android.R.color.transparent, null))
    }

    override fun showErrorIndicator() {
        Toast.makeText(loadingView.context, "An error has occurred", Toast.LENGTH_LONG).show()
    }

    override fun showErrorScreen() {
        loadingView.visibility = View.VISIBLE
        loadingView.text = "ERROR"
        labelView.setBackgroundColor(labelView.resources.getColor(android.R.color.transparent, null))
    }

}
