package com.odai.firecats.cats.view

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.LinearLayout
import com.odai.firecats.R
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.favourite.model.ActionState
import com.odai.firecats.favourite.model.FavouriteState
import com.odai.firecats.favourite.model.FavouriteStatus
import com.odai.firecats.imageloader.Crop
import com.odai.firecats.imageloader.load
import kotlinx.android.synthetic.main.cat_entry_view.view.*

class CatEntryView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    fun display(cat: Cat, favouriteState: FavouriteState, listener: AndroidCatsView.Listener) {
        catLabel.text = cat.name

        displayFavouriteIndicator(cat, favouriteState, listener)

        load(cat.image) {
            cropAs { Crop.CIRCLE_CROP }
            into { avatar }
        }

        setOnClickListener {
            listener.onCatClicked(cat)
        }
    }

    private fun displayFavouriteIndicator(cat: Cat, favouriteState: FavouriteState, listener: AndroidCatsView.Listener) {
        favouriteIndicator.clearColorFilter()
        favouriteIndicator.setOnClickListener { listener.onFavouriteClicked(cat, favouriteState) }
        favouriteIndicator.setImageDrawable(favouriteDrawable(favouriteState.status))
        favouriteIndicator.isEnabled = favouriteState.state == ActionState.CONFIRMED
        if (favouriteState.state == ActionState.PENDING) {
            favouriteIndicator.setColorFilter(R.color.grey, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun favouriteDrawable(favouriteStatus: FavouriteStatus) = resources.getDrawable(when (favouriteStatus) {
        FavouriteStatus.FAVOURITE -> android.R.drawable.star_big_on
        FavouriteStatus.UN_FAVOURITE -> android.R.drawable.star_big_off
    }, null)

}
