/**
 * MIT License
 *
 * Copyright (c) [2021] [Rui Eduardo Soares]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
