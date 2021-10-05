package com.mvi.kotlin.presenter.viewintent

import com.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable

fun interface ViewIntentBinder<V : MviView<*>, T : Any> {

    fun bind(view: V): Observable<T>
}
