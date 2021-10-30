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
