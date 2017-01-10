package com.odai.firecats.cat.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.odai.firecats.R
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.imageloader.Crop
import com.odai.firecats.imageloader.load

class AndroidCatView(context: Context, attrs: AttributeSet): CatView, ImageView(context, attrs) {

    override fun display(cat: Cat) {
        val target = this
        load(cat.image) {
            cropAs { Crop.FIT_CENTER }
            into { target }
        }
    }

}
