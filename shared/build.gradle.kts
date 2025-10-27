plugins {
    alias(libs.plugins.kmp.sample.library.kmp.library)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(projects.core.network)
            implementation(projects.data)
            implementation(projects.feature.home)
        }
    }
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.shared"
}
