# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 言語設定

**重要: このリポジトリでは、必ず以下の言語ルールに従ってください:**
- **思考時（thinking blocks内）**: 英語を使用
- **ユーザーへの出力時**: 日本語を使用
- **コード内のコメント**: 日本語を使用
- **コミットメッセージ**: 日本語を使用

例外: 技術的な用語やAPIドキュメントは英語のまま使用可能

## プロジェクト概要

このプロジェクトは、AndroidとiOSをターゲットとしたKotlin Multiplatform (KMP)ライブラリです。Clean Architectureの原則に従い、レイヤー化されたフィーチャーベースのモジュール構造を採用しています。GitHub Packagesに`io.github.kei_1111.kmp_sample_library`（バージョン1.1.1）として公開されています。

## よく使うコマンド

### ビルド
```bash
# 全モジュールをビルド
./gradlew build

# 特定のモジュールをビルド
./gradlew :core:network:build
./gradlew :feature:home:build

# クリーンビルド
./gradlew clean build
```

### テスト
```bash
# 全モジュールのテストを実行
./gradlew test

# 特定のモジュールのテストを実行
./gradlew :core:network:test
```

### 公開
```bash
# GitHub Packagesに公開（環境変数 GITHUB_ACTOR と GITHUB_TOKEN が必要）
./gradlew publish
```

## アーキテクチャ

### モジュール構成

プロジェクトは以下のレイヤー化されたモジュール構成を採用しています：

```
kmp-sample-library/
├── shared/                    # ルート集約モジュール
├── core/                      # 基盤レイヤー
│   ├── model/                 # kotlinx.serializationを使用したデータモデル
│   ├── domain/                # ビジネスロジックのインターフェースと契約
│   ├── network/               # HTTPクライアント（Ktor）の抽象化
│   └── featurebase/           # フィーチャーモジュール用のベースViewModel
├── data/                      # リポジトリの実装
└── feature/                   # フィーチャー固有のモジュール（UI/ビジネスロジック）
    └── home/                  # フィーチャーモジュールの例
```

**依存関係のフロー（Clean Architectureに従う）:**
- `feature:home` → `core:featurebase` → `core:domain` → `core:model`
- `data` → `core:domain`, `core:model`, `core:network`
- `shared` → `feature:home`, `data`, `core:network`

### build-logic Convention Plugins

プロジェクトは**`includeBuild("build-logic")`**を使用し、`build-logic/convention/`内に4つのカスタムGradle convention pluginsを定義しています：

1. **`kmp.sample.library.android`** (`AndroidPlugin`)
   - Android SDKバージョンを設定: compileSdk=35, minSdk=30
   - Java/JVM互換性をバージョン17に設定

2. **`kmp.sample.library.kmp.library`** (`KmpLibraryPlugin`)
   - 全ライブラリモジュールのベースプラグイン
   - マルチプラットフォームターゲットを設定: androidTarget, iosX64, iosArm64, iosSimulatorArm64
   - AndroidPluginとPublishPluginを自動適用
   - 用途: coreモジュール、dataモジュール、sharedモジュール

3. **`kmp.sample.library.kmp.feature`** (`KmpFeaturePlugin`)
   - フィーチャーモジュール用にKmpLibraryPluginを拡張
   - commonMainに自動追加: `api(":core:featurebase")`、`implementation(":core:domain")`、`implementation(":core:model")`、およびKoin依存関係（koin-core、koin-compose、koin-compose-viewmodel）
   - 用途: フィーチャーモジュール

4. **`kmp.sample.library.publish`** (`PublishPlugin`)
   - GitHub PackagesへのMaven公開を設定
   - リポジトリ: `https://maven.pkg.github.com/kei-1111/kmp-sample-library`
   - 環境変数が必要: `GITHUB_ACTOR`, `GITHUB_TOKEN`
   - バージョンは`gradle/libs.versions.toml`で管理

### どのプラグインを使うべきか

**新しいcore/dataモジュールを作成する場合:**
```kotlin
// build.gradle.kts
plugins {
    alias(libs.plugins.kmp.sample.library.kmp.library)
    // kotlinx.serializationを使用する場合はkotlinSerializationを追加
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // モジュール固有の依存関係を追加
        }
    }
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.core.yourmodule"
}
```

**新しいfeatureモジュールを作成する場合:**
```kotlin
// build.gradle.kts
plugins {
    alias(libs.plugins.kmp.sample.library.kmp.feature)  // core依存関係を自動追加
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // KmpFeaturePluginが提供する依存関係以外の、フィーチャー固有の依存関係のみを追加
        }
    }
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.feature.yourfeature"
}
```

### 依存性注入

プロジェクトは**Koin**を依存性注入に使用しています：
- 各モジュールは`di/ModuleNameModule.kt`内で依存関係を定義
- フィーチャーモジュールは`KmpFeaturePlugin`からKoin依存関係を自動取得
- すべてのKoinモジュールは`shared`モジュールで集約

### テスト構成

- テストはマルチプラットフォームテスト用に**`commonTest`**ソースセット内に配置
- 利用可能なテスト依存関係: `kotlin-test`, `kotlinx-coroutines-test`, `ktor-client-mock`（HTTPモック用）
- テスト構造の例: `core/network/src/commonTest/kotlin/...HttpClientFactoryTest.kt`
- `./gradlew test`で全テストを実行、または`./gradlew :module:test`で特定のモジュールのみ実行

## 主要技術

- **Kotlin 2.2.0** マルチプラットフォームサポート付き
- **Gradle 8.10** configuration cacheとbuild cacheを有効化
- **Compose Multiplatform 1.8.2**（基盤利用可能）
- **Ktor 3.2.0** HTTPクライアント用
- **Koin 4.1.0** 依存性注入用
- **kotlinx.serialization** JSONシリアライゼーション用
- **AndroidX Lifecycle 2.9.1** ViewModelサポート用

## 重要なパターン

### モジュール依存関係
- 常にtype-safe project accessorsを使用: `project(":core:model")`ではなく`projects.core.model`
- フィーチャーモジュールは`core`モジュールにのみ依存すべきで、他の`feature`モジュールには依存しない
- `data`モジュールは`core:domain`, `core:model`, `core:network`に依存

### マルチプラットフォームソースセット
- `commonMain`: 全プラットフォーム共通のコード
- `androidMain`: Android固有の実装
- `iosMain`: iOS固有の実装（iosX64、iosArm64、iosSimulatorArm64で共有）
- `commonTest`: 共通テストコード

### バージョン管理
- すべてのバージョンは`gradle/libs.versions.toml`で管理
- ライブラリのバージョンは`library`バージョンフィールドで制御（現在"1.1.1"）
- バージョンカタログ参照を使用: `libs.koin.core`, `libs.ktor.client.core`など

## CI/CD

- GitHub Actionsワークフロー（`.github/workflows/publish.yml`）がmainブランチへのpush時に実行
- ワークフローは`./gradlew build`を実行後、`./gradlew publish`を実行
- 公開された成果物は他のプロジェクトから利用可能なGitHub Packages上で提供