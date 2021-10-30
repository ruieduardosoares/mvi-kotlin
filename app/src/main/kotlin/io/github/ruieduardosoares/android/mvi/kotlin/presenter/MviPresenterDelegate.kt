package io.github.ruieduardosoares.android.mvi.kotlin.presenter

import androidx.annotation.MainThread
import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent.IntentCreator
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent.IntentCreatorImpl
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent.ViewIntentsBinder
import io.reactivex.rxjava3.core.Observable

@MainThread
internal class MviPresenterDelegate<S : Any, V : MviView<S>>(
    private val mStateRelayToViewRenderBinder: StateRelayToViewRenderBinder<S> =
        StateRelayToViewRenderBinder(),
    private val mStateStreamToStateRelayBinder: StateStreamToStateRelayBinder<S> =
        StateStreamToStateRelayBinder(mStateRelayToViewRenderBinder.getRelay()),
    private val mViewIntentsBinder: ViewIntentsBinder<V> = ViewIntentsBinder(),
    private val mIntentCreator: IntentCreator<V> = IntentCreatorImpl(mViewIntentsBinder)
) {

    private var mView: V? = null

    private var mIntentsBounded = false

    private enum class State {
        ATTACHED, DETACHED, DESTROYED
    }

    private var mState: State = State.DETACHED

    fun attachView(view: V, mviPresenter: AbstractMviPresenter<S, V>) {
        if (mState != State.DETACHED) {
            return
        }
        mView = view
        if (!mIntentsBounded) {
            val stateStream: Observable<S> = mviPresenter.bindIntents()
            mStateStreamToStateRelayBinder.bind(stateStream)
            mIntentsBounded = true
        }
        mStateRelayToViewRenderBinder.bind(view)
        mViewIntentsBinder.bind(view)
        mState = State.ATTACHED
    }

    fun intent(): IntentCreator<V> {
        return mIntentCreator
    }

    fun detachView() {
        if (mState != State.ATTACHED) {
            return
        }
        mStateRelayToViewRenderBinder.dispose()
        mViewIntentsBinder.unbind()
        mView = null
        mState = State.DETACHED
    }

    fun destroy() {
        if (mState == State.DESTROYED) {
            return
        }
        if (mState == State.ATTACHED) {
            detachView()
        }
        mStateStreamToStateRelayBinder.dispose()
        mViewIntentsBinder.destroy()
        mState = State.DESTROYED
    }
}
