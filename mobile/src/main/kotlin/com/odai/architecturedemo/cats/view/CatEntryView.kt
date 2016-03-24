package com.odai.architecturedemo.cats.view

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.odai.architecturedemo.R
import com.odai.architecturedemo.cat.model.Cat
import com.odai.architecturedemo.cats.CatsPresenter
import com.odai.architecturedemo.favourite.model.FavouriteState
import kotlinx.android.synthetic.main.cat_entry_view.view.*

class CatEntryView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    fun display(cat: Cat, favouriteState: FavouriteState, listener: CatsPresenter.CatClickedListener) {
        catLabel.text = cat.name
        catLabel.setOnClickListener {
            listener.onCatClicked(cat)
        }
        favouriteIndicator.clearColorFilter()
        favouriteIndicator.setImageDrawable(favouriteDrawable(favouriteState))
        favouriteIndicator.setOnClickListener { listener.onFavouriteClicked(cat, favouriteState) }
        favouriteIndicator.isEnabled = favouriteState == FavouriteState.FAVOURITE || favouriteState == FavouriteState.UN_FAVOURITE
        if (favouriteState == FavouriteState.PENDING_FAVOURITE || favouriteState == FavouriteState.PENDING_UN_FAVOURITE) {
            favouriteIndicator.setColorFilter(R.color.grey, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun favouriteDrawable(favouriteState: FavouriteState) = resources.getDrawable(when (favouriteState) {
        FavouriteState.FAVOURITE -> android.R.drawable.star_big_on
        FavouriteState.PENDING_FAVOURITE -> android.R.drawable.star_big_on
        FavouriteState.PENDING_UN_FAVOURITE -> android.R.drawable.star_big_off
        FavouriteState.UN_FAVOURITE -> android.R.drawable.star_big_off
    }, null)

}
