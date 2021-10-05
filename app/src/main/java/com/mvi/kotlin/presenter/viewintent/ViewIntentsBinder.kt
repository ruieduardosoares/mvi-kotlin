package com.mvi.kotlin.presenter.viewintent

import com.mvi.kotlin.MviView
import io.reactivex.rxjava3.disposables.CompositeDisposable

internal class ViewIntentsBinder<V : MviView<*>>(
    private val mIntentBinders: MutableList<ViewIntentRelayBinder<V, *>> = mutableListOf(),
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable(),
) {

    fun add(binder: ViewIntentRelayBinder<V, *>) {
        mIntentBinders.add(binder)
    }

    fun bind(view: V) {
        if (mIntentBinders.isEmpty()) return
        unbind()
        mIntentBinders
            .map { intentBinder -> intentBinder.bind(view) }
            .forEach(mCompositeDisposable::add)
    }

    fun unbind() {
        mCompositeDisposable.clear()
    }

    fun destroy() {
        mCompositeDisposable.dispose()
        mIntentBinders.clear()
    }
}
