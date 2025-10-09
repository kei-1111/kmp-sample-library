package io.github.kei_1111.kmp_sample_library.core.featurebase.stateless

import androidx.lifecycle.ViewModel
import io.github.kei_1111.kmp_sample_library.core.featurebase.Action
import io.github.kei_1111.kmp_sample_library.core.featurebase.Effect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow


@Suppress("VariableNaming")
abstract class StatelessBaseViewModel<A : Action, E : Effect> : ViewModel() {

    protected val _effect = Channel<E>(Channel.BUFFERED)

    val effect: Flow<E> = _effect.receiveAsFlow()

    abstract fun onAction(action: A)

    protected fun sendEffect(effect: E) {
        _effect.trySend(effect)
    }
}
