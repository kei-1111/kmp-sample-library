plugins {
    alias(libs.plugins.kmp.sample.library.kmp.library)
    alias(libs.plugins.kmp.sample.library.xcframework)
    alias(libs.plugins.skie)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(projects.core.network)
            implementation(projects.core.data)

            // iOSにexportするため、apiを使用
            api(projects.feature.home)
        }
    }
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.shared"
}
