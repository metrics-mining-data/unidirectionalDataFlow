package com.odai.architecturedemo.cats

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.odai.architecturedemo.CatApplication
import com.odai.architecturedemo.R
import com.odai.architecturedemo.cats.view.AndroidCatsView
import com.odai.architecturedemo.cats.view.CatsView
import com.odai.architecturedemo.loading.LoadingView
import com.odai.architecturedemo.navigation.AndroidNavigator

class CatsActivity : AppCompatActivity() {

    private var _catsPresenter: CatsPresenter? = null

    private var catsPresenter: CatsPresenter
        get() = _catsPresenter!!
        set(value) {
            _catsPresenter = value
        };

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cats)
        val catsView = findViewById(R.id.content) as CatsView
        val loadingView = findViewById(R.id.loadingView) as LoadingView
        catsPresenter = CatsPresenter(
                getCatApplication().catsUseCase,
                getCatApplication().favouriteCatsUseCase,
                AndroidNavigator(this),
                catsView,
                loadingView
        )
    }

    private fun getCatApplication(): CatApplication {
        return application as CatApplication
    }

    override fun onResume() {
        super.onResume()
        catsPresenter.startPresenting()
    }

    override fun onPause() {
        super.onPause()
        catsPresenter.stopPresenting()
    }

}
