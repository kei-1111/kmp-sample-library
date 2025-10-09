package io.github.kei_1111.kmp_sample_library.feature.home

import androidx.lifecycle.viewModelScope
import io.github.kei_1111.kmp_sample_library.core.domain.data.MarsRepository
import io.github.kei_1111.kmp_sample_library.core.featurebase.stateful.StatefulBaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val marsRepository: MarsRepository,
) : StatefulBaseViewModel<HomeViewModelState, HomeState, HomeAction, HomeEffect>() {

    override fun createInitialViewModelState(): HomeViewModelState = HomeViewModelState()
    override fun createInitialState(): HomeState = HomeState.Init

    init {
        loadMarsProperties()
    }

    private fun loadMarsProperties() {
        updateViewModelState { copy(statusType = HomeViewModelState.StatusType.LOADING) }
        viewModelScope.launch {
            val properties = marsRepository.getProperties()
            updateViewModelState {
                copy(
                    statusType = HomeViewModelState.StatusType.STABLE,
                    marsProperties = properties
                )
            }
        }
    }

    override fun onAction(action: HomeAction) {

    }
}
