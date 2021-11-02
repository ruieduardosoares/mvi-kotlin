package com.mvi.sample

import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter
import io.reactivex.rxjava3.core.Observable

class LaunchMviPresenter : AbstractMviPresenter<LaunchViewState, LaunchView>() {

    override fun bindIntents(): Observable<LaunchViewState> {

        val animateLogoStream = intent().create { view -> view.animateLogoIntent() }
            .map { LaunchViewState.AnimateLogoState }

        val animateLogoTextStream = intent().create { view -> view.animateLogoTextIntent() }
            .concatMap {
                Observable.just(
                    LaunchViewState.AnimateLogoTextState,
                    LaunchViewState.AnimationFinished
                )
            }

        return Observable.merge(animateLogoStream, animateLogoTextStream)
    }

    // state reducer...
    // etc...
}
