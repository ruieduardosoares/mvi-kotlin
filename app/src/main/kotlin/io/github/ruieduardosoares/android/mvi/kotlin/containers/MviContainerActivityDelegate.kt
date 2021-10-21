package io.github.ruieduardosoares.android.mvi.kotlin.containers

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModelProvider
import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter

@MainThread
internal class MviContainerActivityDelegate<S : Any, V : MviView<S>>(
    private val mMviContainerActivity: MviContainerActivity<S, V>,
    private val mLazyMviPresenterMemento: Lazy<MviContainerPresenterMemento> =
        lazyOf(MviContainerPresenterMemento(ViewModelProvider(mMviContainerActivity)))
) {

    private val mMviView: V = mMviContainerActivity.getView()

    private var mMviPresenter: AbstractMviPresenter<S, V>? = null

    fun onCreate(savedInstanceState: Bundle?) {
        mMviPresenter = loadPresenter(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadPresenter(savedInstanceState: Bundle?): AbstractMviPresenter<S, V> =
        if (savedInstanceState == null) {
            mMviContainerActivity.createPresenter()
        } else {
            val mviPresenter = mLazyMviPresenterMemento.value.recover()
            mviPresenter?.run { this as AbstractMviPresenter<S, V> }
                ?: mMviContainerActivity.createPresenter()
        }

    fun onStart() {
        mMviPresenter?.attachView(mMviView)
    }

    fun onStop() {
        mMviPresenter?.let { presenter ->
            val isChangingConfigurations = mMviContainerActivity.isChangingConfigurations
            presenter.detachView(isChangingConfigurations.not())
            if (isChangingConfigurations) {
                mLazyMviPresenterMemento.value.keep(presenter)
            }
        }
    }

    fun onDestroy() {
        if (mMviContainerActivity.isChangingConfigurations.not()) {
            mMviPresenter?.destroy()
        }
        mMviPresenter = null
    }
}
