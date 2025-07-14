package io.github.kei_1111.kmp_sample_library.feature.home.di

import io.github.kei_1111.kmp_sample_library.feature.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
}