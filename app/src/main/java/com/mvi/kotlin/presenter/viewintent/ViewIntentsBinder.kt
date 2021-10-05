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
