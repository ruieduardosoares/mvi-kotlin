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
