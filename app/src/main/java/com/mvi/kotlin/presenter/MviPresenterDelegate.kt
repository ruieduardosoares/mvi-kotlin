package com.mvi.kotlin.presenter

import androidx.annotation.MainThread
import com.mvi.kotlin.MviView
import com.mvi.kotlin.presenter.viewintent.IntentCreator
import com.mvi.kotlin.presenter.viewintent.IntentCreatorImpl
import com.mvi.kotlin.presenter.viewintent.ViewIntentsBinder
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

    fun detachView(isViewPermanentlyDetached: Boolean, mviPresenter: AbstractMviPresenter<S, V>) {
        if (mState != State.ATTACHED) {
            return
        }
        mStateRelayToViewRenderBinder.dispose()
        mViewIntentsBinder.unbind()
        mView = null
        if (isViewPermanentlyDetached) {
            mviPresenter.unbindIntents()
        }
        mState = State.DETACHED
    }

    fun destroy(mviPresenter: AbstractMviPresenter<S, V>) {
        if (mState == State.DESTROYED) {
            return
        }
        if (mState == State.ATTACHED) {
            detachView(true, mviPresenter)
        }
        mStateStreamToStateRelayBinder.dispose()
        mViewIntentsBinder.destroy()
        mState = State.DESTROYED
    }
}
