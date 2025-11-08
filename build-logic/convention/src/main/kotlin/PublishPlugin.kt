import io.github.kei_1111.kmp_sample_library.versions
import io.github.kei_1111.kmp_sample_library.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.ByteArrayOutputStream
import java.io.File

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

                createXCFrameworkTask()
            }
        }
    }

    private fun Project.createXCFrameworkTask() {
        // モジュール名の最初の文字を大文字に（KmpLibraryPluginと同じロジック）
        val xcframeworkName = project.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(java.util.Locale.ROOT) else it.toString()
        }

        // configuration cache対応のため、Providerとして取得
        val buildDirProvider = layout.buildDirectory
        val outputDirProvider = layout.buildDirectory.dir("outputs")

        tasks.register("packageXCFramework") {
            group = "publishing"
            description = "iOS用のXCFrameworkをzip化してchecksumを計算"

            // Kotlin Multiplatformプラグインが自動生成するタスクに依存
            dependsOn("assemble${xcframeworkName}ReleaseXCFramework")

            doLast {
                val buildDir = buildDirProvider.get().asFile
                val outputPath = outputDirProvider.get().asFile

                // 自動生成されたXCFramework
                val xcframework = File(buildDir, "XCFrameworks/release/${xcframeworkName}.xcframework")
                val outputZip = File(outputPath, "${xcframeworkName}.xcframework.zip")

                // 出力ディレクトリ作成
                outputPath.mkdirs()

                // 既存のzipを削除
                if (outputZip.exists()) {
                    outputZip.delete()
                }

                // XCFrameworkをzip圧縮
                project.exec {
                    workingDir(xcframework.parentFile)
                    commandLine("zip", "-r", outputZip.absolutePath, xcframework.name)
                }

                // checksumを計算
                val checksumOutput = ByteArrayOutputStream()
                project.exec {
                    commandLine("swift", "package", "compute-checksum", outputZip.absolutePath)
                    standardOutput = checksumOutput
                }
                val checksum = checksumOutput.toString().trim()

                // checksumをファイルに保存
                File(outputPath, "checksum.txt").writeText(checksum)

                logger.lifecycle("✅ XCFramework: ${xcframework.absolutePath}")
                logger.lifecycle("📦 Zip作成完了: ${outputZip.absolutePath}")
                logger.lifecycle("🔑 Checksum: $checksum")
            }
        }
    }
}