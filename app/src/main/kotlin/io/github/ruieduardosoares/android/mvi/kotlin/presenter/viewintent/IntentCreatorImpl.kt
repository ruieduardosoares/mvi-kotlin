package io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.UnicastSubject

internal class IntentCreatorImpl<V : MviView<*>>(
    private val mViewIntentsBinder: ViewIntentsBinder<V>
) : IntentCreator<V> {

    override fun <I : Any> create(viewIntentBinder: ViewIntentBinder<V, I>): Observable<I> {
        val relaySubject = UnicastSubject.create<I>()
        mViewIntentsBinder.add(ViewIntentRelayBinder(viewIntentBinder, IntentRelay(relaySubject)))
        return relaySubject.hide()
    }
}
