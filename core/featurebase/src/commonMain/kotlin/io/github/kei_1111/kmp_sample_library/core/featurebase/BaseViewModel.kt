package io.github.kei_1111.kmp_sample_library.core.featurebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : State, A : Action, E : Effect>(
    initialState: S,
) : ViewModel() {
    protected val _uiState = MutableStateFlow<S>(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    protected val _effect = MutableSharedFlow<E>()
    val effect: SharedFlow<E> = _effect.asSharedFlow()

    fun updateState(update: (S) -> S) {
        _uiState.update { update(it) }
    }

    abstract fun onAction(action: A)

    fun sendEffect(effect: E) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}