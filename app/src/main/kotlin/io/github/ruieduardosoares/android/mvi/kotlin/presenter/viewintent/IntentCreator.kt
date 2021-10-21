package io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable

interface IntentCreator<V : MviView<*>> {

    fun <I : Any> create(viewIntentBinder: ViewIntentBinder<V, I>): Observable<I>
}
