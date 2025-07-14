package io.github.kei_1111.kmp_sample_library.data.di

import io.github.kei_1111.kmp_sample_library.core.domain.data.MarsRepository
import io.github.kei_1111.kmp_sample_library.data.MarsRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<MarsRepository> { MarsRepositoryImpl(get()) }
}