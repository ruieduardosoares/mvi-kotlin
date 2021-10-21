package io.github.ruieduardosoares.android.mvi.kotlin.containers

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter
import java.util.concurrent.atomic.AtomicReference

@MainThread
internal class MviContainerPresenterMemento(viewModelProvider: ViewModelProvider) {

    private val mPresenterStoreModel: PresenterStoreModel =
        viewModelProvider.get(PresenterStoreModel::class.java)

    fun keep(presenter: AbstractMviPresenter<*, *>) =
        mPresenterStoreModel.presenterReference.set(presenter)

    fun recover(): AbstractMviPresenter<*, *>? =
        mPresenterStoreModel.presenterReference.getAndSet(null)

    @MainThread
    internal class PresenterStoreModel : ViewModel() {

        val presenterReference = AtomicReference<AbstractMviPresenter<*, *>>()
    }
}
