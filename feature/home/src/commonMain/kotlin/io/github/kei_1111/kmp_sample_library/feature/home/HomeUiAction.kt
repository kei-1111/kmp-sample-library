package io.github.kei_1111.kmp_sample_library.feature.home

import io.github.kei_1111.kmp_sample_library.core.featurebase.UiAction
import io.github.kei_1111.kmp_sample_library.feature.home.model.MarsPropertyUiModel

sealed interface HomeUiAction : UiAction {
    data class OnClickMarsPropertyCard(val marsProperty: MarsPropertyUiModel) : HomeUiAction

    data object OnDismissMarsPropertyDetailDialog : HomeUiAction
}