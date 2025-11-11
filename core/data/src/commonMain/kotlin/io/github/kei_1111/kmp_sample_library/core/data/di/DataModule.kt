package io.github.kei_1111.kmp_sample_library.core.data.di

import io.github.kei_1111.kmp_sample_library.core.data.MarsRepositoryImpl
import io.github.kei_1111.kmp_sample_library.core.domain.data.MarsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single<MarsRepository> { MarsRepositoryImpl(get()) }
}