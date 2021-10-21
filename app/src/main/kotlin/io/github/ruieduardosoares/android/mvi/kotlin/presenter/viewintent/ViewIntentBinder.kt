package io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable

fun interface ViewIntentBinder<V : MviView<*>, T : Any> {

    fun bind(view: V): Observable<T>
}
