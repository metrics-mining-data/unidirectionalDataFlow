package com.odai.architecturedemo.cat.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.odai.architecturedemo.cat.model.Cat

class AndroidCatView(context: Context, attrs: AttributeSet): CatView, TextView(context, attrs) {

    override fun display(cat: Cat) {
        text = cat.name
    }

}
