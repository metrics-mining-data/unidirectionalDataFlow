package com.odai.architecturedemo.cats.view

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.odai.architecturedemo.R
import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.CatsPresenter
import com.odai.architecturedemo.favourite.model.FavouriteState

class CatEntryView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var _labelView: TextView? = null
    private var _favouriteView: ImageView? = null

    private var labelView: TextView
        get() = _labelView!!
        set(value) {
            _labelView = value
        };

    private var favouriteView: ImageView
        get() = _favouriteView!!
        set(value) {
            _favouriteView = value
        };

    override fun onFinishInflate() {
        super.onFinishInflate()
        labelView = findViewById(R.id.catLabel) as TextView
        favouriteView = findViewById(R.id.favouriteIndicator) as ImageView
    }

    fun display(cat: Cat, favouriteState: FavouriteState, listener: CatsPresenter.CatClickedListener) {
        labelView.text = cat.name
        labelView.setOnClickListener {
            listener.onCatClicked(cat)
        }
        favouriteView.clearColorFilter()
        favouriteView.setImageDrawable(favouriteDrawable(favouriteState))
        favouriteView.setOnClickListener { listener.onFavouriteClicked(cat, favouriteState) }
        favouriteView.isEnabled = favouriteState == FavouriteState.FAVOURITE || favouriteState == FavouriteState.UN_FAVOURITE
        if (favouriteState == FavouriteState.PENDING_FAVOURITE || favouriteState == FavouriteState.PENDING_UN_FAVOURITE) {
            favouriteView.setColorFilter(R.color.grey, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun favouriteDrawable(favouriteState: FavouriteState) = resources.getDrawable(when (favouriteState) {
        FavouriteState.FAVOURITE -> android.R.drawable.star_big_on
        FavouriteState.PENDING_FAVOURITE -> android.R.drawable.star_big_on
        FavouriteState.PENDING_UN_FAVOURITE -> android.R.drawable.star_big_off
        FavouriteState.UN_FAVOURITE -> android.R.drawable.star_big_off
    }, null)

}
