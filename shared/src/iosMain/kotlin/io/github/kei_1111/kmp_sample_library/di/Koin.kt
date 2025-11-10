package io.github.kei_1111.kmp_sample_library.di

import io.github.kei_1111.kmp_sample_library.core.network.di.networkModule
import io.github.kei_1111.kmp_sample_library.data.di.dataModule
import io.github.kei_1111.kmp_sample_library.feature.home.HomeViewModel
import io.github.kei_1111.kmp_sample_library.feature.home.di.homeModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

actual fun initKoin(appContext: Any?) {
    startKoin {
        modules(networkModule, dataModule, homeModule)
    }.koin
}

object ViewModelProvider: KoinComponent {
    fun provideHomeViewModel(): HomeViewModel = get()
}