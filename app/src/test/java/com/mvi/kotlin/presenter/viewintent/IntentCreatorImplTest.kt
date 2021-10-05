package com.mvi.kotlin.presenter.viewintent

import com.mvi.kotlin.MviView
import io.reactivex.rxjava3.internal.operators.observable.ObservableHide
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.only
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import java.util.function.Consumer

@ExtendWith(MockitoExtension::class)
internal class IntentCreatorImplTest {

    @Mock
    lateinit var mViewIntentsBinder: ViewIntentsBinder<MviView<Any>>

    @Mock
    lateinit var mViewIntentBinder: ViewIntentBinder<MviView<Any>, Any>

    @InjectMocks
    lateinit var mIntentCreator: IntentCreatorImpl<MviView<Any>>

    @Test
    internal fun create_thenReturnObservableHide() {

        //When
        val intentObservable = mIntentCreator.create(mViewIntentBinder)

        //Then
        verify(mViewIntentsBinder, only()).add(any())
        intentObservable.shouldBeInstanceOf<ObservableHide<Any>>()
    }

    @Test
    internal fun create_whenSubscribedOnce_thenAllowed() {

        //Given
        val intentObservable = mIntentCreator.create(mViewIntentBinder)
        val errorConsumer = mock<Consumer<Any>>()

        //When
        intentObservable.subscribe({}, errorConsumer::accept, {})

        //Verify
        verifyZeroInteractions(errorConsumer)
    }

    @Test
    internal fun create_whenSubscribedMoreThenOnce_thenNotAllowed() {

        //Given
        val intentObservable = mIntentCreator.create(mViewIntentBinder)
        val errorConsumer = mock<Consumer<Any>>()
        intentObservable.subscribe()

        //When
        intentObservable.subscribe({}, errorConsumer::accept, {})

        //Verify
        val argumentCaptor = argumentCaptor<IllegalStateException>()
        verify(errorConsumer, only()).accept(argumentCaptor.capture())
        val exception = argumentCaptor.firstValue
        exception.shouldNotBeNull().shouldBeInstanceOf<IllegalStateException>()
        exception.message.shouldBeEqualTo("Only a single observer allowed.")
    }
}
