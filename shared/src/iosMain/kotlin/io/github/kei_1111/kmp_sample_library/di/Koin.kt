package io.github.kei_1111.kmp_sample_library.di

import io.github.kei_1111.kmp_sample_library.core.network.di.networkModule
import io.github.kei_1111.kmp_sample_library.data.di.dataModule
import io.github.kei_1111.kmp_sample_library.feature.home.di.homeModule
import org.koin.core.Koin
import org.koin.core.context.startKoin

actual fun initKoin(appContext: Any?): Koin = startKoin {
    modules(networkModule, dataModule, homeModule)
}.koin