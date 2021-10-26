package io.github.ruieduardosoares.android.mvi.kotlin.containers

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter

/**
 * Mvi container activity that merely acts as a container for the view and the presenter.
 * This container shouldn't be your view implementation but rather a container for your view.
 *
 * Example
 * ```
 * class SomeViewContainerActivity : MviContainerActivity<SomeViewState, SomeMviView>() {
 *
 *     private lateinit var mSomeMviView: SomeMviView
 *
 *     private lateinit var mBinding: ActivitySomeViewBinding
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *
 *         //Using android viewBinding
 *         mBinding = ActivitySomeViewBinding.inflate(layoutInflater)
 *         setContentView(mBinding.root)
 *
 *         mSomeMviView = SomeMviView(mBinding)
 *     }
 *
 *     override fun getView(): SomeMviView = mSomeMviView
 *
 *     override fun createPresenter(): SomeMviPresenter = SomeMviPresenter()
 * }
 * ```
 * For more implementation details see [MviContainerActivityDelegate]
 *
 * @param S the type of the view state that will be used
 * @param V the type of the MviView that will be used
 */
@MainThread
abstract class MviContainerActivity<S : Any, V : MviView<S>> : AppCompatActivity() {

    private val mDelegate by lazy { MviContainerActivityDelegate(this) }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
    }

    /**
     * You must implement this method and provide and view instance of [V] type.
     * It should return the same view instance as long your activity is not destroyed.
     *
     * @return view instance of [V] type
     */
    abstract fun getView(): V

    /**
     * You must implement this method and provide a new instance of the given presenter.
     * It should return a new instance of an implementation of [AbstractMviPresenter].
     *
     * @return new instance of an implementation of [AbstractMviPresenter]
     */
    abstract fun createPresenter(): AbstractMviPresenter<S, V>

    @CallSuper
    override fun onStart() {
        super.onStart()
        mDelegate.onStart()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        mDelegate.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        mDelegate.onDestroy()
    }
}
