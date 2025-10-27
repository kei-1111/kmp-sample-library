plugins {
    alias(libs.plugins.kmp.sample.library.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel)
        }
    }
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.core.featurebase"
}
