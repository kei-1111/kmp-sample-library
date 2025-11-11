# KMP Sample Library

AndroidとiOSをターゲットとしたKotlin Multiplatform (KMP)ライブラリです。Clean Architectureの原則に従い、レイヤー化されたフィーチャーベースのモジュール構造を採用しています。

## 📦 インストール

各プラットフォームでの使用方法については、[ANDROID_USAGE.md](docs/ANDROID_USAGE.md)、[IOS_USAGE.md](docs/IOS_USAGE.md)を参照してください。

## 🚀 実装例：Mars Real Estate API

このライブラリは、[Mars Real Estate API](https://android-kotlin-fun-mars-server.appspot.com)を使用した実装例を含んでいます。Clean Architectureに基づいた以下のレイヤー構成で、APIデータ取得から状態管理までを実装しています。

### 実装の流れ

```
UI Layer (feature:home)
    ↓ HomeViewModel - StatefulBaseViewModelで状態管理
Domain Layer (core:domain)
    ↓ MarsRepository - データ取得のインターフェース
Data Layer (core:data)
    ↓ MarsRepositoryImpl - リポジトリの実装
Network Layer (core:network)
    ↓ MarsApiServiceImpl - Ktorを使用したHTTPクライアント
```

### 主要コンポーネント

- **MarsProperty** (`core:model`): 火星不動産データモデル（kotlinx.serialization使用）
- **MarsApiService** (`core:network`): Ktorを使ったAPI通信
- **MarsRepository** (`core:data`): データ取得ロジックの実装
- **HomeViewModel** (`feature:home`): StatefulBaseViewModelを継承した状態管理

この実装により、レイヤー間の依存関係が明確に分離され、テスタビリティと保守性の高いコードを実現しています。

## 🏗️ アーキテクチャ

### モジュール構成

```
kmp-sample-library/
├── shared/                    # Koinエントリポイントモジュール
├── core/                      # 基盤レイヤー
│   ├── model/                 # データモデル（kotlinx.serialization使用）
│   ├── domain/                # ビジネスロジックのインターフェースと契約
│   ├── data/                  # リポジトリの実装
│   ├── network/               # HTTPクライアント（Ktor）の抽象化
│   └── featurebase/           # フィーチャーモジュール用のベースViewModel
└── feature/                   # フィーチャー固有のモジュール
    └── home/                  # フィーチャーモジュールの例
```

## 🛠️ 技術スタック

| カテゴリ | 技術 | バージョン |
|---------|------|-----------|
| 言語 | Kotlin Multiplatform | 2.2.0 |
| ビルドツール | Gradle | 8.10 |
| HTTP Client | Ktor | 3.2.0 |
| 依存性注入 | Koin | 4.1.0 |
| シリアライゼーション | kotlinx.serialization | - |
| ViewModel | AndroidX Lifecycle | 2.9.1 |

## 🚀 セットアップ

### 必要な環境

- JDK 17以上
- Android Studio Ladybug以降
- Xcode 15以上（iOS開発の場合）

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

## 📝 Convention Plugins

プロジェクトは`build-logic`内にカスタムGradle convention pluginsを定義しています：

### 1. `kmp.sample.library.android`
- Android SDKバージョンを設定（compileSdk=35, minSdk=30）
- Java/JVM互換性をバージョン17に設定

### 2. `kmp.sample.library.kmp.library`
- 全ライブラリモジュールのベースプラグイン
- マルチプラットフォームターゲット: Android, iOS (x64, arm64, simulator arm64)
- coreモジュール、sharedモジュールで使用

### 3. `kmp.sample.library.kmp.feature`
- フィーチャーモジュール用プラグイン
- core依存関係とKoin依存関係を自動追加
- featureモジュールで使用

### 4. `kmp.sample.library.publish`
- GitHub PackagesへのMaven公開を設定

## 🔧 新しいモジュールの作成

### Coreモジュールの場合

```kotlin
// build.gradle.kts
plugins {
    alias(libs.plugins.kmp.sample.library.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // モジュール固有の依存関係
        }
    }
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.core.yourmodule"
}
```

### Featureモジュールの場合

```kotlin
// build.gradle.kts
plugins {
    alias(libs.plugins.kmp.sample.library.kmp.feature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // フィーチャー固有の追加依存関係のみ
        }
    }
}

android {
    namespace = "io.github.kei_1111.kmp_sample_library.feature.yourfeature"
}
```

## 🧪 テスト構成

- テストは`commonTest`ソースセット内に配置
- 利用可能なテスト依存関係：
  - `kotlin-test`
  - `kotlinx-coroutines-test`
  - `ktor-client-mock`（HTTPモック用）

```bash
# 全テストを実行
./gradlew test

# 特定のモジュールのテストを実行
./gradlew :core:network:test
```

## 📚 依存性注入（Koin）

- 各モジュールは`di/${MODULE_NAME}Module.kt`内で依存関係を定義
- フィーチャーモジュールはKoin依存関係を自動取得
- すべてのKoinモジュールは`shared`モジュールで集約

```kotlin
// di/YourModule.kt
val yourModule = module {
    single<YourRepository> { YourRepositoryImpl(get(), ...) }
}
```

## 🔄 CI/CD

GitHub Actionsワークフローがmainブランチへのpush時に自動実行されます：

1. `./gradlew build`でプロジェクトをビルド
2. `./gradlew publish`でGitHub Packagesに公開

ワークフローファイル: `.github/workflows/publish.yml`

## 📖 ドキュメント

- [Android使用方法](docs/ANDROID_USAGE.md) - Android側での使用方法
- [iOS使用方法](docs/IOS_USAGE.md) - iOS側での使用方法
- [CLAUDE.md](CLAUDE.md) - AI開発支援ツール用のプロジェクトガイド
