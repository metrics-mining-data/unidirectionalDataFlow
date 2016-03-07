package com.odai.architecturedemo

import android.app.Application
import com.odai.architecturedemo.UseCase.CatUseCase
import com.odai.architecturedemo.api.CatApi
import com.odai.architecturedemo.api.FakeCatsApi
import com.odai.architecturedemo.persistence.CatRepository
import com.odai.architecturedemo.persistence.InMemoryCatRepo

class CatApplication : Application() {

    val api : CatApi = FakeCatsApi()
    val repository : CatRepository = InMemoryCatRepo()
    val catUseCase: CatUseCase = CatUseCase(api, repository)

}
