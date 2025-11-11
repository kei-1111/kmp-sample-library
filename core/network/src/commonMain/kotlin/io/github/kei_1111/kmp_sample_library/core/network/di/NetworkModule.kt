package io.github.kei_1111.kmp_sample_library.core.network.di

import io.github.kei_1111.kmp_sample_library.core.domain.network.MarsApiService
import io.github.kei_1111.kmp_sample_library.core.network.api.MarsApiServiceImpl
import io.github.kei_1111.kmp_sample_library.core.network.client.HttpClientFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory.create() }
    single<MarsApiService> { MarsApiServiceImpl(get()) }
}
