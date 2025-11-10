import io.github.kei_1111.kmp_sample_library.versions
import io.github.kei_1111.kmp_sample_library.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class PublishPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "maven-publish")

            group = "io.github.kei_1111.kmp_sample_library"
            version = libs.versions("library")

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    publishLibraryVariants("release")
                }
            }

            afterEvaluate {
                extensions.configure<PublishingExtension> {
                    repositories {
                        maven {
                            name = "GitHubPackages"
                            url = uri("https://maven.pkg.github.com/kei-1111/kmp-sample-library")
                            credentials {
                                username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String?
                                password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.token") as String?
                            }
                        }
                    }
                }
            }
        }
    }
}