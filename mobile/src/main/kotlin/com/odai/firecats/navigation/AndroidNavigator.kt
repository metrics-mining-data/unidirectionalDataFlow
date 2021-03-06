package com.odai.firecats.navigation

import android.app.Activity
import android.content.Intent
import com.odai.firecats.cat.CatActivity
import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.CatsActivity

class AndroidNavigator(private val activity: Activity): Navigator {

    companion object {
        val ID_EXTRA: String = "TODO_CHANGE_AS_URI_NAVIGATION"
    }

    override fun toCats() {
        val intent = Intent(activity, CatsActivity::class.java)
        activity.startActivity(intent)
    }

    override fun toCat(cat: Cat) {
        val intent = Intent(activity, CatActivity::class.java)
        intent.putExtra(AndroidNavigator.ID_EXTRA, cat.id)
        activity.startActivity(intent)
    }

}
