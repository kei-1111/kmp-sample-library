plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    // kmp-sample-library
    alias(libs.plugins.kmp.sample.library.android) apply false
    alias(libs.plugins.kmp.sample.library.kmp.library) apply false
    alias(libs.plugins.kmp.sample.library.kmp.feature) apply false
    alias(libs.plugins.kmp.sample.library.publish) apply false
}