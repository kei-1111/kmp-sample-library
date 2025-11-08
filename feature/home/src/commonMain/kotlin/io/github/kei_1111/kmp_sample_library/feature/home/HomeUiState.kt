package io.github.kei_1111.kmp_sample_library.feature.home

import io.github.kei_1111.kmp_sample_library.core.featurebase.stateful.UiState
import io.github.kei_1111.kmp_sample_library.feature.home.model.MarsPropertyUiModel

sealed interface HomeUiState : UiState {
    data object Init : HomeUiState

    data object Loading : HomeUiState

    data class Stable(
        val marsPropertyUiModels: List<MarsPropertyUiModel>,
        val selectedPropertyUiModel: MarsPropertyUiModel? = null,
    ) : HomeUiState

    data class Error(
        val message: String
    ) : HomeUiState
}