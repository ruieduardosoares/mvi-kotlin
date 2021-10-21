package io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent

import io.reactivex.rxjava3.observers.DefaultObserver
import io.reactivex.rxjava3.subjects.Subject

internal class IntentRelay<T : Any>(private val mRelaySubject: Subject<T>) : DefaultObserver<T>() {

    override fun onNext(t: T) = mRelaySubject.onNext(t)

    override fun onError(e: Throwable) = throw IllegalStateException("View must not emit errors", e)

    override fun onComplete() = mRelaySubject.onComplete()

    fun copy(): IntentRelay<T> = IntentRelay(mRelaySubject)
}
