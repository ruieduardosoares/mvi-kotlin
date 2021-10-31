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
import io.reactivex.rxjava3.core.Observable

/**
 * Abstract mvi presenter, this is the base presenter class you must extend in order to be
 * compliant with the established MVI contract.
 *
 * @param S the view state type, the instance type that is eventually passed to view.render(state)
 * @param V the specific MviView contract declaration for the given presenter
 *
 */
@MainThread
abstract class AbstractMviPresenter<S : Any, V : MviView<S>> {

    private val mMviPresenterDelegate = MviPresenterDelegate<S, V>()

    /**
     * Attaches the [V] view instance.
     *
     * For more details see [MviPresenterDelegate.attachView].
     *
     * @param view the [V] instance to attach
     */
    fun attachView(view: V) {
        mMviPresenterDelegate.attachView(view, this)
    }

    /**
     * You must override this method and build up your state stream, leave the rest to us.
     *
     *
     * Example:
     * ```
     * class SomeMviPresenter: AbstractMviPresenter<SomeViewState, SomeMviView>() {
     *
     *      override fun bindIntents(): Observable<SomeViewState> {
     *
     *          // bind view intents with business logic to build state stream
     *          // see below more details
     *
     *      }
     * }
     * ```
     * @return the fully built state stream as an Observable
     */
    abstract fun bindIntents(): Observable<S>

    /**
     * This is the method you must use to bind the view [V] specific intent methods with your
     * business respective business logic
     *
     * Example:
     * ```
     * //Given this view
     * class SomeView : MviView<SomeViewState> {
     *
     *      fun showNumberIntent(): Observable<Int> = Observable.just(1)
     *
     *      fun hideNumberIntent(): Observable<Int> = Observable.just(1)
     *
     *      fun render(state: SomeViewState) {
     *
     *          // render state the way you need
     *      }
     * }
     *
     * // When used in presenter
     * class SomeMviPresenter: AbstractMviPresenter<SomeViewState, SomeMviView>() {
     *
     *      override fun bindIntents(): Observable<SomeViewState> {
     *
     *          val showNumberStream = intent().create { view -> view.showNumberIntent() }
     *              .map { number -> SomeViewState.ShowNumber(number) }
     *
     *          val hideNumberStream = intent().create { view -> view.hideNumberIntent() }
     *              .map { number -> SomeViewState.HideNumber(number) }
     *
     *          return Observable.merge(showStuffStream, hideStuffStream)
     *      }
     * }
     * ```
     * @return the intent creator through which you bind the view intent with the business logic
     */
    protected fun intent(): IntentCreator<V> {
        return mMviPresenterDelegate.intent()
    }

    /**
     * Detaches the previously [V] view instance
     *
     * For more details see [MviPresenterDelegate.detachView].
     */
    fun detachView() {
        mMviPresenterDelegate.detachView()
    }

    /**
     * Destroys the presenter
     *
     * For more details see [MviPresenterDelegate.destroy].
     */
    fun destroy() {
        mMviPresenterDelegate.destroy()
    }
}
