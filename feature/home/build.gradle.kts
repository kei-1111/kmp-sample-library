plugins {
    alias(libs.plugins.kmp.sample.library.kmp.feature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.featurebase)
            implementation(projects.core.domain)
            implementation(projects.core.model)

            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core)
        }
    }
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.feature.home"
}
