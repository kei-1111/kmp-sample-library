import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kmp.sample.library.kmp.library)
    alias(libs.plugins.skie)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(projects.core.network)
            implementation(projects.data)
            api(projects.feature.home)
        }
    }
}

// KmpLibraryPluginが作成したフレームワークにexportを追加
afterEvaluate {
    kotlin.targets.withType<KotlinNativeTarget>().configureEach {
        binaries.withType<Framework>().configureEach {
            export(projects.feature.home)
        }
    }
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.shared"
}
