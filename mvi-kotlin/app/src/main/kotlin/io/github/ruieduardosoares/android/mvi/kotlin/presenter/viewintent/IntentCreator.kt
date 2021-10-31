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

package io.github.ruieduardosoares.android.mvi.kotlin.presenter.viewintent

import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.reactivex.rxjava3.core.Observable

/**
 * This class is responsible for creating an intent based on the [ViewIntentBinder].
 * Implementation details can be seen on [IntentCreatorImpl]
 *
 * An implementation instance of this class is provided in [AbstractMviPresenter][io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter]
 * in order to build the view state stream
 *
 * @param V is the type of MviView that
 * @constructor Create empty Intent creator
 */
interface IntentCreator<V : MviView<*>> {

    /**
     * Creates the intent stream based on the given [viewIntentBinder]
     *
     * @param I the type of the intent from the "view intent" method
     * @param viewIntentBinder the binder which contains the intent of the view
     * @return an Observable of the given view intent type back to the caller
     */
    fun <I : Any> create(viewIntentBinder: ViewIntentBinder<V, I>): Observable<I>
}
