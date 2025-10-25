package io.github.kei_1111.kmp_sample_library.feature.home

import io.github.kei_1111.kmp_sample_library.core.featurebase.Action
import io.github.kei_1111.kmp_sample_library.core.model.MarsProperty

sealed interface HomeAction : Action {
    data class OnClickMarsPropertyCard(val marsProperty: MarsProperty) : HomeAction
}