package com.odai.firecats.cat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.odai.firecats.CatApplication
import com.odai.firecats.R
import com.odai.firecats.navigation.AndroidNavigator
import kotlinx.android.synthetic.main.activity_cat.*

class CatActivity : AppCompatActivity() {

    private lateinit var catPresenter: CatPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat)
        val id = intent.getIntExtra(AndroidNavigator.ID_EXTRA, -1)
        if(id == -1) throw IllegalArgumentException("Intent should contain the cat id")
        catPresenter = CatPresenter(id, getCatApplication().catService, content, loadingView)
    }

    private fun getCatApplication(): CatApplication {
        return application as CatApplication
    }

    override fun onResume() {
        super.onResume()
        catPresenter.startPresenting()
    }

    override fun onPause() {
        super.onPause()
        catPresenter.stopPresenting()
    }

}
