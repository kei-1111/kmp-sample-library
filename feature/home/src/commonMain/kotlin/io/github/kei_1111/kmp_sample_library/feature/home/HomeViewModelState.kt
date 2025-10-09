package io.github.kei_1111.kmp_sample_library.feature.home

import io.github.kei_1111.kmp_sample_library.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.kmp_sample_library.core.model.MarsProperty

data class HomeViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val marsProperties: List<MarsProperty> = emptyList(),
    val errorMessage: String? = null
) : ViewModelState<HomeState> {
    enum class StatusType { IDLE, LOADING, STABLE, ERROR}

    override fun toState() = when (statusType) {
        StatusType.IDLE -> HomeState.Init

        StatusType.LOADING -> HomeState.Loading

        StatusType.STABLE -> HomeState.Success(marsProperties)

        StatusType.ERROR -> HomeState.Error(errorMessage ?: "Unknown error")
    }
}
