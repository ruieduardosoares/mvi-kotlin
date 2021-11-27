package com.mvi.sample

import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class LaunchMviPresenter : AbstractMviPresenter<LaunchViewState, LaunchView>() {

    override fun bindIntents(): Observable<LaunchViewState> {

        val animateLogoStream = intent().create { view -> view.animateLogoIntent() }
            .map { LaunchViewState.AnimateLogoState }

        val animateLogoTextStream = intent().create { view -> view.animateLogoTextIntent() }
            .map<LaunchViewState> { LaunchViewState.AnimateLogoTextState }
            .mergeWith(
                Observable.just(LaunchViewState.AnimationFinished)
                    .delay(4, TimeUnit.SECONDS)
            ).observeOn(AndroidSchedulers.mainThread())

        return Observable.merge(animateLogoStream, animateLogoTextStream)
    }

    // state reducer...
    // etc...
}
