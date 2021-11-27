package io.github.ruieduardosoares.android.mvi.kotlin.containers

import androidx.lifecycle.ViewModelStoreOwner
import io.github.ruieduardosoares.android.mvi.kotlin.MviView
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter

internal interface MviContainer<S : Any, V : MviView<S>> : ViewModelStoreOwner {

    fun getView(): V

    fun createPresenter(): AbstractMviPresenter<S, V>

    fun isSurvivable(): Boolean
}
