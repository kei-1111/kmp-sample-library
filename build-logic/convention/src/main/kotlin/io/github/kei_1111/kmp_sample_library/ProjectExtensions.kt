package io.github.kei_1111.kmp_sample_library

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

internal fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

internal fun DependencyHandler.api(dependencyNotation: Any): Dependency? =
    add("api", dependencyNotation)

internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.versions(name: String): String =
    this.findVersion(name).get().requiredVersion

internal fun VersionCatalog.library(name: String) =
    this.findLibrary(name).get().get()
