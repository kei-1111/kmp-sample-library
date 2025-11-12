import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.process.ExecOperations
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

/**
 * XCFramework作成とパッケージング用のプラグイン
 * このプロジェクトでは:sharedのiOSフレームワークを配布する必要があるモジュールに適用します
 */
class XCFrameworkPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // フレームワークのexport設定
            afterEvaluate {
                extensions.configure<KotlinMultiplatformExtension> {
                    targets.withType<KotlinNativeTarget>().configureEach {
                        // 注: このプラグインを適用するモジュールは、
                        // exportしたいモジュールをapi依存関係として追加する必要があります
                        binaries.withType<Framework>().configureEach {
                            export(project(":feature:home"))
                        }
                    }
                }
            }

            // packageXCFrameworkタスクを登録
            tasks.register<PackageXCFrameworkTask>("packageXCFramework") {
                group = "publishing"
                description = "iOS用のXCFrameworkをzip化してchecksumを計算"
                dependsOn("assembleSharedReleaseXCFramework")
            }
        }
    }
}

/**
 * XCFrameworkをzip化してchecksumを計算するタスク
 */
abstract class PackageXCFrameworkTask : DefaultTask() {
    @get:Inject
    abstract val execOperations: ExecOperations

    @TaskAction
    fun packageXCFramework() {
        val buildDir = project.layout.buildDirectory.get().asFile
        val outputDir = File(buildDir, "outputs").apply { mkdirs() }

        val xcframework = File(buildDir, "XCFrameworks/release/Shared.xcframework")
        val outputZip = File(outputDir, "Shared.xcframework.zip")

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
        File(outputDir, "checksum.txt").writeText(checksum)

        logger.lifecycle("✅ XCFramework: ${xcframework.absolutePath}")
        logger.lifecycle("📦 Zip作成完了: ${outputZip.absolutePath}")
        logger.lifecycle("🔑 Checksum: $checksum")
    }
}