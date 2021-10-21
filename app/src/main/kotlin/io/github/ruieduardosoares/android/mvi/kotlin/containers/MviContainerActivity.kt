package io.github.ruieduardosoares.android.mvi.kotlin.containers

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter

@MainThread
abstract class MviContainerActivity<S : Any, V : MviView<S>> : AppCompatActivity() {

    private val mDelegate by lazy { MviContainerActivityDelegate(this) }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
    }

    abstract fun getView(): V

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
