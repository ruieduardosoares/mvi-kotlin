package com.mvi.kotlin.presenter.viewintent

import com.mvi.kotlin.MviView
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.SafeObserver

internal class ViewIntentRelayBinder<V : MviView<*>, T : Any>(
    private val mViewIntentBinder: ViewIntentBinder<V, T>,
    private val mIntentRelay: IntentRelay<T>,
) {

    private var mRelayPreviouslyUsed = false

    fun bind(view: V): Disposable {
        val intentRelay = if (mRelayPreviouslyUsed) mIntentRelay.copy() else mIntentRelay
        return mViewIntentBinder.bind(view)
            .subscribeWith(SafeObserver(intentRelay))
            .also { mRelayPreviouslyUsed = true }
    }
}
