plugins {
    alias(libs.plugins.kmp.sample.library.kmp.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.model"
}
