package io.github.kei_1111.kmp_sample_library.feature.home

import io.github.kei_1111.kmp_sample_library.core.featurebase.stateful.State
import io.github.kei_1111.kmp_sample_library.core.model.MarsProperty

sealed interface HomeState : State {
    data object Init : HomeState

    data object Loading : HomeState

    data class Stable(
        val marsProperties: List<MarsProperty>
    ) : HomeState

    data class Error(
        val message: String
    ) : HomeState
}