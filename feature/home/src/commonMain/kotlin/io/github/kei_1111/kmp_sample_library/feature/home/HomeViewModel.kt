package io.github.kei_1111.kmp_sample_library.feature.home

import androidx.lifecycle.viewModelScope
import io.github.kei_1111.kmp_sample_library.core.domain.data.MarsRepository
import io.github.kei_1111.kmp_sample_library.core.featurebase.BaseViewModel
import io.github.kei_1111.kmp_sample_library.core.model.MarsProperty
import kotlinx.coroutines.launch

class HomeViewModel(
    private val marsRepository: MarsRepository,
) : BaseViewModel<HomeState, HomeAction, HomeEffect>(HomeState.Init) {

    init {
        loadMarsProperties()
    }

    private fun loadMarsProperties() {
        updateState { HomeState.Loading }
        viewModelScope.launch {
            val properties = marsRepository.getProperties()
            updateState { HomeState.Success(properties) }
        }
    }

    override fun onAction(action: HomeAction) {

    }
}
