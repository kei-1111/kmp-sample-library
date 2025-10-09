package io.github.kei_1111.kmp_sample_library.core.featurebase.stateful

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kei_1111.kmp_sample_library.core.featurebase.Action
import io.github.kei_1111.kmp_sample_library.core.featurebase.Effect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("VariableNaming")
abstract class StatefulBaseViewModel<VS : ViewModelState<S>, S : State, A : Action, E : Effect> : ViewModel() {

    protected val _viewModelState = MutableStateFlow<VS>(createInitialViewModelState())

    val state: StateFlow<S> = _viewModelState
        .map(ViewModelState<S>::toState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = createInitialState(),
        )

    protected val _effect = Channel<E>(Channel.BUFFERED)

    val effect: Flow<E> = _effect.receiveAsFlow()

    protected abstract fun createInitialViewModelState(): VS

    protected abstract fun createInitialState(): S

    abstract fun onAction(action: A)

    protected fun updateViewModelState(update: VS.() -> VS) {
        _viewModelState.update { update(it) }
    }

    protected fun sendEffect(effect: E) {
        _effect.trySend(effect)
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}