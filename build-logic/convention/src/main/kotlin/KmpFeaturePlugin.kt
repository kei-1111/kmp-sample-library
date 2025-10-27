import io.github.kei_1111.kmp_sample_library.library
import io.github.kei_1111.kmp_sample_library.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "kmp.sample.library.kmp.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain.dependencies {
                        api(project(":core:featurebase"))
                        implementation(project(":core:domain"))
                        implementation(project(":core:model"))

                        implementation(libs.library("koin-core"))
                        implementation(libs.library("koin-compose"))
                        implementation(libs.library("koin-compose-viewmodel"))
                    }
                }
            }
        }
    }
}