package io.github.kei_1111.kmp_sample_library

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform