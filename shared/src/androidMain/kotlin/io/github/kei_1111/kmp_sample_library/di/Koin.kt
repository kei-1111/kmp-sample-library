package io.github.kei_1111.kmp_sample_library.di

import android.content.Context
import io.github.kei_1111.kmp_sample_library.core.network.di.networkModule
import io.github.kei_1111.kmp_sample_library.data.di.dataModule
import io.github.kei_1111.kmp_sample_library.feature.home.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.context.GlobalContext.startKoin

actual fun initKoin(appContext: Any?): Koin = startKoin {
    appContext?.let { androidContext(it as Context) }
    modules(networkModule, dataModule, homeModule)
}.koin