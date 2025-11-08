package io.github.kei_1111.kmp_sample_library.core.featurebase.stateful

interface ViewModelState<S : UiState> {
    fun toState(): S
}