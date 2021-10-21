package io.github.ruieduardosoares.android.mvi.kotlin.presenter

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DefaultObserver
import io.reactivex.rxjava3.observers.SafeObserver
import io.reactivex.rxjava3.subjects.BehaviorSubject

internal class StateRelayToViewRenderBinder<S : Any>(
    private val mStateRelaySubject: BehaviorSubject<S> = BehaviorSubject.create()
) {

    private var mDisposable: Disposable? = null

    fun bind(view: MviView<S>) {
        mDisposable?.let { disposable -> if (disposable.isDisposed.not()) return }
        mDisposable = mStateRelaySubject.subscribeWith(SafeObserver(ViewRenderObserver(view)))
    }

    fun getRelay(): BehaviorSubject<S> = mStateRelaySubject

    fun dispose() {
        mDisposable = mDisposable?.let {
            it.dispose()
            null
        }
    }

    internal class ViewRenderObserver<S : Any>(private val mView: MviView<S>) :
        DefaultObserver<S>() {

        override fun onNext(state: S) {
            mView.render(state)
        }

        override fun onError(throwable: Throwable) {
            throw IllegalStateException(
                "Error must not reach view but rather handled into a proper error state.", throwable
            )
        }

        override fun onComplete() {}
    }
}
