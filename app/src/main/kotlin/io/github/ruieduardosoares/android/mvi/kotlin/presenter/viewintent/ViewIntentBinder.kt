package io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable

/**
 * This interface is responsible for binding the MviView declared "intent method" with the view state stream
 *
 * This is tightly used with [IntentCreator].
 *
 * It is declared as a functional interface so that SAM conversions can be used to simplify code
 *
 * @param V the type of MviView
 * @param T the type of data the MviView "intent method" emits
 */
fun interface ViewIntentBinder<V : MviView<*>, T : Any> {

    /**
     * Binds the view intent method of the view
     *
     * @param view the MviView type from which we call the respective view "intent method"
     * @return an Observable of the given view "intent method" supported data type
     */
    fun bind(view: V): Observable<T>
}
