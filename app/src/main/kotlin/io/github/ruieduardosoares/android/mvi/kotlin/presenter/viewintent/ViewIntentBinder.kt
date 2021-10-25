package io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable

/**
 * This interface is responsible for binding the MviView declared "intent method" with the view pipeline
 *
 * This is tightly used with [IntentCreator].
 *
 * It is declared as a functional interface so that SAM conversions can be used to simplify code
 *
 * @param V the type of MviView
 * @param T the type of data the MviView "intent method" emits
 */
fun interface ViewIntentBinder<V : MviView<*>, T : Any> {

    fun bind(view: V): Observable<T>
}
