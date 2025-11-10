import org.gradle.process.ExecOperations
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.io.ByteArrayOutputStream
import javax.inject.Inject

plugins {
    alias(libs.plugins.kmp.sample.library.kmp.library)
    alias(libs.plugins.skie)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(projects.core.network)
            implementation(projects.core.data)

            // iOSにexportするため、apiを使用
            api(projects.feature.home)
        }
    }
}

// KmpLibraryPluginが作成したフレームワークにexportを追加
afterEvaluate {
    kotlin.targets.withType<KotlinNativeTarget>().configureEach {
        binaries.withType<Framework>().configureEach {
            export(projects.feature.home)
        }
    }
}

// XCFrameworkをzip化してchecksumを計算するタスク
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

tasks.register<PackageXCFrameworkTask>("packageXCFramework") {
    group = "publishing"
    description = "iOS用のXCFrameworkをzip化してchecksumを計算"
    dependsOn("assembleSharedReleaseXCFramework")
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.shared"
}
