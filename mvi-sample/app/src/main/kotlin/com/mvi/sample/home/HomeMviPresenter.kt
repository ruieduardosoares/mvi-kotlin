package com.mvi.sample.home

import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter
import io.reactivex.rxjava3.core.Observable

class HomeMviPresenter : AbstractMviPresenter<HomeViewState, HomeView>() {

    override fun bindIntents(): Observable<HomeViewState> {

        return Observable.empty()
    }

    // state reducer...
    // etc...
}
