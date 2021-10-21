package io.github.ruieduardosoares.android.mvi.kotlin.presenter

import androidx.annotation.MainThread
import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent.IntentCreator
import io.reactivex.rxjava3.core.Observable

@MainThread
abstract class AbstractMviPresenter<S : Any, V : MviView<S>> {

    private val mMviPresenterDelegate = MviPresenterDelegate<S, V>()

    fun attachView(view: V) {
        mMviPresenterDelegate.attachView(view, this)
    }

    abstract fun bindIntents(): Observable<S>

    open fun unbindIntents() {}

    protected fun intent(): IntentCreator<V> {
        return mMviPresenterDelegate.intent()
    }

    fun detachView(isViewPermanentlyDetached: Boolean) {
        mMviPresenterDelegate.detachView(isViewPermanentlyDetached, this)
    }

    fun destroy() {
        mMviPresenterDelegate.destroy(this)
    }
}
