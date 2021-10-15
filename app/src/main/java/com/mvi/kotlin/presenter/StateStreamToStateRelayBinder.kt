package com.mvi.kotlin.presenter

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

internal class StateStreamToStateRelayBinder<S : Any>(
    private val mViewRenderRelaySubject: BehaviorSubject<S>
) {

    private var mDisposable: Disposable? = null

    fun bind(stateStream: Observable<S>) {
        mDisposable?.let { disposable -> if (disposable.isDisposed.not()) return }
        mDisposable =
            stateStream.subscribe(
                mViewRenderRelaySubject::onNext,
                mViewRenderRelaySubject::onError,
                mViewRenderRelaySubject::onComplete
            )
    }

    fun dispose() {
        mDisposable = mDisposable?.let {
            it.dispose()
            null
        }
    }
}
