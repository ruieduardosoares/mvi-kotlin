package io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable

/**
 * This class is responsible for creating an intent based on the [ViewIntentBinder].
 * Implementation details can be seen on [IntentCreatorImpl]
 *
 * An implementation instance of this class is provided in [AbstractMviPresenter][io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter]
 * in order to build the view state stream
 *
 * @param V is the type of MviView that
 * @constructor Create empty Intent creator
 */
interface IntentCreator<V : MviView<*>> {

    /**
     * Creates the intent stream based on the given [viewIntentBinder]
     *
     * @param I the type of the intent from the "view intent" method
     * @param viewIntentBinder the binder which contains the intent of the view
     * @return an Observable of the given view intent type back to the caller
     */
    fun <I : Any> create(viewIntentBinder: ViewIntentBinder<V, I>): Observable<I>
}
