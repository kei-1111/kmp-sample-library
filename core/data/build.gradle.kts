plugins {
    alias(libs.plugins.kmp.sample.library.kmp.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.model)
            implementation(projects.core.network)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.core.data"
}
