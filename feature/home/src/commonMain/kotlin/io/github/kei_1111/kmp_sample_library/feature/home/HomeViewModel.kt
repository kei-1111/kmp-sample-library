package io.github.kei_1111.kmp_sample_library.feature.home

import androidx.lifecycle.viewModelScope
import io.github.kei_1111.kmp_sample_library.core.domain.data.MarsRepository
import io.github.kei_1111.kmp_sample_library.core.featurebase.stateful.StatefulBaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val marsRepository: MarsRepository,
) : StatefulBaseViewModel<HomeViewModelState, HomeUiState, HomeUiAction, HomeUiEffect>() {

    override fun createInitialViewModelState(): HomeViewModelState = HomeViewModelState()
    override fun createInitialUiState(): HomeUiState = HomeUiState.Init

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

    override fun onAction(action: HomeUiAction) {
        when(action) {
            is HomeUiAction.OnClickMarsPropertyCard -> {
                updateViewModelState {
                    copy(
                        selectedProperty = _viewModelState.value.marsProperties.find { it.id == action.marsProperty.id }
                    )
                }
            }

            is HomeUiAction.OnDismissMarsPropertyDetailDialog -> {
                updateViewModelState {
                    copy(
                        selectedProperty = null
                    )
                }
            }
        }
    }
}
