package io.github.ruieduardosoares.android.mvi.kotlin.containers

import androidx.lifecycle.ViewModelProvider
import io.github.ruieduardosoares.android.mvi.kotlin.presenter.AbstractMviPresenter
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.only
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.concurrent.atomic.AtomicReference

@ExtendWith(MockitoExtension::class)
internal class MviPresenterMementoTest {

    @Mock
    lateinit var mViewModelProvider: ViewModelProvider

    @Mock
    lateinit var mPresenter: AbstractMviPresenter<*, *>

    @Mock
    lateinit var mPresenterStoreModel: MviContainerPresenterMemento.PresenterStoreModel

    @Mock
    lateinit var mPresenterReference: AtomicReference<AbstractMviPresenter<*, *>>

    lateinit var mMemento: MviContainerPresenterMemento

    @BeforeEach
    internal fun setUp() {
        whenever(mPresenterStoreModel.presenterReference).thenReturn(mPresenterReference)
        whenever(mViewModelProvider.get(eq(MviContainerPresenterMemento.PresenterStoreModel::class.java)))
            .thenReturn(mPresenterStoreModel)
        mMemento = MviContainerPresenterMemento(mViewModelProvider)
    }

    @Test
    internal fun keep_thenSetPresenterIntoStoreModelReference() {

        //When
        mMemento.keep(mPresenter)

        //Then
        verify(mPresenterStoreModel, only()).presenterReference
        verify(mPresenterReference, only()).set(eq(mPresenter))
    }

    @Test
    internal fun recover_thenGetPresenterAndClearItFromStoreModel() {

        //Given
        whenever(mPresenterReference.getAndSet(eq(null))).thenReturn(mPresenter)

        //When
        val presenter = mMemento.recover()

        //Then
        presenter.shouldNotBeNull()
        verify(mPresenterReference, only()).getAndSet(eq(null))
    }
}
