package com.mvi.sample.home.fragment

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable

interface HomeView : MviView<HomeViewState> {

    fun displayInputTextIntent(): Observable<String>

    fun clearTextIntent(): Observable<Unit>
}
