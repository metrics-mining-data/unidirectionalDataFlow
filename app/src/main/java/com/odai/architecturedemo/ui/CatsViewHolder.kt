package com.odai.architecturedemo.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.odai.architecturedemo.R
import com.odai.architecturedemo.model.Cat

class CatsViewHolder(itemView: TextView?) : RecyclerView.ViewHolder(itemView) {
    fun bind(cat: Cat, isFavourite: Boolean, listener: MainActivity.CatClickedListener) {
        val textView = itemView as TextView
        textView.text = cat.name
        textView.setBackgroundColor(if(isFavourite) R.color.colorAccent else android.R.color.transparent);
        textView.setOnClickListener {
            listener.onCatClicked(cat)
        }
    }
}
