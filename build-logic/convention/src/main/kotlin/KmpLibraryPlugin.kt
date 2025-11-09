import io.github.kei_1111.kmp_sample_library.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import java.util.Locale

class KmpLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.multiplatform")
            apply(plugin = "skie")
            apply(plugin = "kmp.sample.library.android")
            apply(plugin = "kmp.sample.library.publish")

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    @OptIn(ExperimentalKotlinGradlePluginApi::class)
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_17)
                    }
                }

                // すべてのモジュールでXCFrameworkを作成
                // モジュール名の最初の文字を大文字に（例: "shared" -> "Shared", "model" -> "Model"）
                val frameworkBaseName = project.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                }

                val xcf = XCFramework(frameworkBaseName)
                listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = frameworkBaseName
                        isStatic = true
                        xcf.add(this)
                    }
                }
            }
        }
    }
}