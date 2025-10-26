package io.github.kei_1111.kmp_sample_library.feature.home

import io.github.kei_1111.kmp_sample_library.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.kmp_sample_library.core.model.MarsProperty
import io.github.kei_1111.kmp_sample_library.feature.home.model.MarsPropertyUiModel

data class HomeViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val marsProperties: List<MarsProperty> = emptyList(),
    val selectedProperty: MarsProperty? = null,
    val errorMessage: String? = null
) : ViewModelState<HomeState> {
    enum class StatusType { IDLE, LOADING, STABLE, ERROR}

    override fun toState() = when (statusType) {
        StatusType.IDLE -> HomeState.Init

        StatusType.LOADING -> HomeState.Loading

        StatusType.STABLE -> HomeState.Stable(
            marsProperties = marsProperties.map { MarsPropertyUiModel.convert(it) },
            selectedProperty = selectedProperty?.let { MarsPropertyUiModel.convert(it) },
        )

        StatusType.ERROR -> HomeState.Error(errorMessage ?: "Unknown error")
    }
}
