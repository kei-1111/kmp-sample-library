import io.github.kei_1111.kmp_sample_library.versions
import io.github.kei_1111.kmp_sample_library.libs
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.process.ExecOperations
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

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
        val frameworkName = name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(java.util.Locale.ROOT) else it.toString()
        }

        tasks.register<PackageXCFrameworkTask>("packageXCFramework") {
            group = "publishing"
            description = "iOS用のXCFrameworkをzip化してchecksumを計算"

            xcframeworkName.set(frameworkName)
            buildDir.set(layout.buildDirectory)
            outputDir.set(layout.buildDirectory.dir("outputs"))

            dependsOn("assemble${frameworkName}ReleaseXCFramework")
        }
    }
}

/**
 * XCFrameworkをzip化してchecksumを計算するタスク
 * Configuration cache対応のため、ExecOperationsを注入
 */
abstract class PackageXCFrameworkTask : DefaultTask() {
    @get:Inject
    abstract val execOperations: ExecOperations

    @get:Input
    abstract val xcframeworkName: Property<String>

    @get:InputDirectory
    abstract val buildDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun packageXCFramework() {
        val buildDirFile = buildDir.get().asFile
        val outputPath = outputDir.get().asFile
        val frameworkName = xcframeworkName.get()

        // 自動生成されたXCFramework
        val xcframework = File(buildDirFile, "XCFrameworks/release/${frameworkName}.xcframework")
        val outputZip = File(outputPath, "${frameworkName}.xcframework.zip")

        // 出力ディレクトリ作成
        outputPath.mkdirs()

        // 既存のzipを削除
        if (outputZip.exists()) {
            outputZip.delete()
        }

        // XCFrameworkをzip圧縮
        execOperations.exec {
            workingDir(xcframework.parentFile)
            commandLine("zip", "-r", outputZip.absolutePath, xcframework.name)
        }

        // checksumを計算
        val checksumOutput = ByteArrayOutputStream()
        execOperations.exec {
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