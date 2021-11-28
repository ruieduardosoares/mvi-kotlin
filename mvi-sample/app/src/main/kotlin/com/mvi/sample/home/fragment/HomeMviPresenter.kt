package com.mvi.sample.home.fragment

import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter
import io.reactivex.rxjava3.core.Observable

class HomeMviPresenter : AbstractMviPresenter<HomeViewState, HomeView>() {

    override fun bindIntents(): Observable<HomeViewState> {

        val displayTextStream = intent().create { view -> view.displayInputTextIntent() }
            .map<HomeViewState> { HomeViewState.DisplayInputText(it) }

        val clearTextStream = intent().create { view -> view.clearTextIntent() }
            .map<HomeViewState> { HomeViewState.ClearText }

        return displayTextStream.mergeWith(clearTextStream)
    }
}
