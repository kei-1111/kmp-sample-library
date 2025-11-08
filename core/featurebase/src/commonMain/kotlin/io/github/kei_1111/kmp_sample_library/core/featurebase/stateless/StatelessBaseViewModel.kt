package io.github.kei_1111.kmp_sample_library.core.featurebase.stateless

import androidx.lifecycle.ViewModel
import io.github.kei_1111.kmp_sample_library.core.featurebase.UiAction
import io.github.kei_1111.kmp_sample_library.core.featurebase.UiEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow


@Suppress("VariableNaming")
abstract class StatelessBaseViewModel<A : UiAction, E : UiEffect> : ViewModel() {

    protected val _effect = Channel<E>(Channel.BUFFERED)

    val effect: Flow<E> = _effect.receiveAsFlow()

    abstract fun onAction(action: A)

    protected fun sendEffect(effect: E) {
        _effect.trySend(effect)
    }
}
