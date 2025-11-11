# Androidでの使用方法

このドキュメントでは、KMP Sample LibraryをAndroidプロジェクトで使用する方法を説明します。

## 目次

1. [提供されているライブラリ](#提供されているライブラリ)
2. [GitHub Packagesでの使用](#github-packagesでの使用)
3. [Kotlinから使用](#kotlinから使用)
4. [リリース手順](#リリース手順)
5. [トラブルシューティング](#トラブルシューティング)

---

## 提供されているライブラリ

このプロジェクトでは、以下のライブラリをGitHub Packages経由で提供しています：

- **Shared** - アプリ全体の基盤（Koin初期化など）
- **Feature Modules** - 各フィーチャーモジュール（`:feature:home`など）
- **Core Modules** - コアモジュール（必要に応じて）

**重要**: Androidでは、モジュールごとに対応するKMPライブラリを追加する必要があります。

**モジュール構成の例**:
- Androidの`:app`モジュール → KMPライブラリの`:shared`を依存追加
- Androidの`:feature:home`モジュール → KMPライブラリの`:feature:home`を依存追加
  - HomeViewModel、HomeUiStateなどのKMPライブラリの機能を使用可能

---

## GitHub Packagesでの使用

GitHub Packagesから公開されたライブラリを使用します。

### 前提条件

- GitHubアカウントを持っている
- GitHub Personal Access Token（PAT）を持っている
  - `read:packages`スコープが必要
  - トークンの作成: [GitHub Settings → Developer settings → Personal access tokens](https://github.com/settings/tokens)

### 1. GitHub Packagesの認証情報を設定

プロジェクトルートの`~/local.properties`に以下を追加：

```properties
gpr.user=あなたのGitHubユーザー名
gpr.token=あなたのGitHub Personal Access Token
```

**注意**: `local.properties`を`.gitignore`に追加し、トークンがコミットされないようにしてください。


### 2. リポジトリを追加

プロジェクトの`settings.gradle.kts`にGitHub Packagesリポジトリを追加：

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/kei-1111/kmp-sample-library")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.token").orNull ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

### 3. 依存関係を追加

各モジュールの`build.gradle.kts`に対応するKMPライブラリを追加：

#### `:app`モジュールの場合

```kotlin
// app/build.gradle.kts
dependencies {
    // KMPライブラリのSharedモジュール（必須）
    implementation("io.github.kei_1111.kmp_sample_library:shared:1.3.1")

    // Koin依存関係
    implementation("io.insert-koin:koin-android:4.1.0")
    implementation("io.insert-koin:koin-androidx-compose:4.1.0")
}
```

#### Androidの`:feature:home`モジュールの場合

```kotlin
// feature/home/build.gradle.kts
dependencies {
    // KMPライブラリのfeature-homeモジュール
    implementation("io.github.kei_1111.kmp_sample_library:feature-home:1.3.1")

    // Koin依存関係
    implementation("io.insert-koin:koin-android:4.1.0")
    implementation("io.insert-koin:koin-androidx-compose:4.1.0")
}
```

#### 他のフィーチャーモジュールの場合（現在他のフィーチャーモジュールはありません）

```kotlin
// feature/profile/build.gradle.kts
dependencies {
    implementation("io.github.kei_1111.kmp_sample_library:feature-profile:1.3.1")

    implementation("io.insert-koin:koin-android:4.1.0")
    implementation("io.insert-koin:koin-androidx-compose:4.1.0")
}

// feature/settings/build.gradle.kts
dependencies {
    implementation("io.github.kei_1111.kmp_sample_library:feature-settings:1.3.1")

    implementation("io.insert-koin:koin-android:4.1.0")
    implementation("io.insert-koin:koin-androidx-compose:4.1.0")
}
```

**重要**:
- `:app`モジュールには`:shared`を追加
- 各Androidフィーチャーモジュールには、対応するKMPライブラリのフィーチャーモジュールを追加

### 4. 同期してビルド

```bash
./gradlew sync
./gradlew build
```

---

## Kotlinから使用

### 1. アプリケーションクラスでKoinを初期化

```kotlin
import android.app.Application
import io.github.kei_1111.kmp_sample_library.shared.initKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Koinを初期化（必須）
        initKoin(appContext = this)
    }
}
```

`AndroidManifest.xml`にアプリケーションクラスを登録：

```xml
<application
    android:name=".MyApplication"
    ...>
    ...
</application>
```

### 2. ViewModelをActivityで使用

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.github.kei_1111.kmp_sample_library.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    // KoinからViewModelを取得
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.state.collectAsState()

            // UIを構築
            HomeScreen(state = state)
        }
    }
}
```

### 3. Jetpack ComposeでViewModelを使用

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import io.github.kei_1111.kmp_sample_library.feature.home.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen() {
    // KoinからViewModelを取得
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is HomeUiState.Init -> {
                Text("Welcome to Home Screen")
            }

            is HomeUiState.Loading -> {
                CircularProgressIndicator()
                Text("Loading...")
            }

            is HomeUiState.Stable -> {
                val stableState = state as HomeUiState.Stable
                Text("Stable State")
                // stableStateのデータを使用
            }

            is HomeUiState.Error -> {
                val errorState = state as HomeUiState.Error
                Text(
                    text = "Error: ${errorState.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
```

---

## リリース手順

新しいバージョンをリリースする際の手順です。**GitHub Actionsで自動化されています。**

### 1. バージョンを更新

`gradle/libs.versions.toml`でライブラリのバージョンを更新します：

```toml
[versions]
# ...
library = "1.3.1"  # 新しいバージョンに変更
```

### 2. 変更をコミット＆プッシュ

```bash
git add gradle/libs.versions.toml
git commit -m "chore: バージョンを1.3.1に更新"
git push origin main
```

### 3. タグを作成してプッシュ

```bash
git tag v1.3.1
git push origin v1.3.1
```

### 4. GitHub Actionsが自動実行

タグがプッシュされると、GitHub Actions（`.github/workflows/android-release.yml`）が自動的に:

1. ✅ プロジェクトをビルド
2. ✅ すべてのモジュール（`:shared`、`:feature:*`、`:core:*`）をGitHub Packagesに公開

### 5. リリースを確認

GitHub Packagesページで新しいバージョンが公開されていることを確認：
- https://github.com/kei-1111/kmp-sample-library/packages

---

## トラブルシューティング

### ビルドエラー: `Could not resolve io.github.kei_1111.kmp_sample_library:*`

#### 原因1: GitHub認証情報が正しくない

`local.properties`または環境変数を確認してください：

```bash
# 環境変数を確認
echo $GITHUB_ACTOR
echo $GITHUB_TOKEN
```

#### 原因2: Personal Access Tokenのスコープが不足

GitHub Personal Access Tokenに`read:packages`スコープがあることを確認してください。

#### 原因3: モジュール名が間違っている

正しいモジュール名を使用しているか確認：
- `:feature:home` → `feature-home`
- `:core:domain` → `core-domain`
- `:shared` → `shared`

#### 原因4: ネットワークの問題

```bash
# キャッシュをクリア
./gradlew --refresh-dependencies
```

### Koin初期化エラー: `KoinAppAlreadyStartedException`

Koinが複数回初期化されています。

**解決策**:
- `initKoin()`を`Application`クラスの`onCreate()`で1回だけ呼び出す
- テストでKoinを初期化している場合は、各テスト後に`stopKoin()`を呼び出す

### ViewModelが見つからない: `NoBeanDefFoundException`

#### 原因1: Koinが初期化されていない

`Application`クラスで`initKoin()`を呼び出しているか確認してください。

#### 原因2: フィーチャーモジュールが追加されていない

使用したいフィーチャーのモジュールを`build.gradle.kts`に追加しているか確認：

```kotlin
dependencies {
    implementation("io.github.kei_1111.kmp_sample_library:shared:1.3.1")
    implementation("io.github.kei_1111.kmp_sample_library:feature-home:1.3.1")  // 必要
}
```

#### 原因3: Koin依存関係が不足

`build.gradle.kts`に以下の依存関係を追加：

```kotlin
dependencies {
    implementation("io.insert-koin:koin-android:4.1.0")
    implementation("io.insert-koin:koin-androidx-compose:4.1.0")
}
```

### Gradle Sync失敗: `Could not GET 'https://maven.pkg.github.com/...'`

#### 原因1: インターネット接続の問題

インターネット接続を確認してください。

#### 原因2: GitHub Packagesへのアクセス権限がない

- リポジトリが公開されているか確認
- Personal Access Tokenが有効か確認
- トークンに`read:packages`スコープがあるか確認

#### 原因3: プロキシ設定

プロキシ環境下では、`gradle.properties`にプロキシ設定を追加：

```properties
systemProp.http.proxyHost=your.proxy.host
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=your.proxy.host
systemProp.https.proxyPort=8080
```

### ビルドエラー: `Duplicate class found`

複数のモジュールが同じクラスを提供している可能性があります。

**解決策**:
- 依存関係の重複を確認: `./gradlew :app:dependencies`
- 不要なモジュールを削除

### マルチプラットフォーム型の不一致

KMPライブラリの型（`Flow`, `StateFlow`など）がAndroidの型と異なる場合があります。

**解決策**:
- `kotlinx-coroutines-core`と`kotlinx-coroutines-android`のバージョンを統一
- ライブラリと同じバージョンのKotlinとCoroutinesを使用

```kotlin
// build.gradle.kts
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
}
```

### クラスが見つからない: `ClassNotFoundException`

特定のフィーチャーのクラスが見つからない場合、該当するフィーチャーモジュールが追加されているか確認：

```kotlin
// HomeViewModelを使用する場合
dependencies {
    implementation("io.github.kei_1111.kmp_sample_library:shared:1.3.1")
    implementation("io.github.kei_1111.kmp_sample_library:feature-home:1.3.1")  // 必須
}
```

### R8/ProGuard最適化によるクラッシュ

リリースビルドで動作しない場合、ProGuardルールを追加：

```proguard
# proguard-rules.pro

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Koin
-keep class org.koin.** { *; }
-keep class io.github.kei_1111.kmp_sample_library.** { *; }

# KMP Library - すべてのモジュール
-keep class io.github.kei_1111.kmp_sample_library.shared.** { *; }
-keep class io.github.kei_1111.kmp_sample_library.feature.** { *; }
-keep class io.github.kei_1111.kmp_sample_library.core.** { *; }
```

---

## 利用可能なモジュール一覧

このライブラリで提供されている主なモジュール：

### 必須モジュール
- `io.github.kei_1111.kmp_sample_library:shared:VERSION` - Koin初期化などの基盤

### フィーチャーモジュール
- `io.github.kei_1111.kmp_sample_library:feature-home:VERSION` - Homeフィーチャー

### コアモジュール（必要に応じて）
- `io.github.kei_1111.kmp_sample_library:core-model:VERSION` - データモデル
- `io.github.kei_1111.kmp_sample_library:core-domain:VERSION` - ドメインレイヤー
- `io.github.kei_1111.kmp_sample_library:core-data:VERSION` - データレイヤー
- `io.github.kei_1111.kmp_sample_library:core-network:VERSION` - ネットワークレイヤー
- `io.github.kei_1111.kmp_sample_library:core-featurebase:VERSION` - フィーチャーのベース

**注意**: 通常は`:shared`と使用したい`:feature:*`モジュールのみを追加すれば十分です。`:core:*`モジュールは`:feature:*`に含まれています。

---

## 参考情報

- [Kotlin Multiplatform公式ドキュメント](https://kotlinlang.org/docs/multiplatform.html)
- [GitHub Packages with Gradle](https://docs.github.com/ja/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry)
- [Koinドキュメント](https://insert-koin.io/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)