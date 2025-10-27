plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false

    // kmp-sample-library
    alias(libs.plugins.kmp.sample.library.android) apply false
    alias(libs.plugins.kmp.sample.library.kmp.library) apply false
    alias(libs.plugins.kmp.sample.library.kmp.feature) apply false
    alias(libs.plugins.kmp.sample.library.publish) apply false
}