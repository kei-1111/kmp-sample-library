# iOSでの使用方法

このドキュメントでは、KMP Sample LibraryをiOSプロジェクトで使用する方法を説明します。

## 目次

1. [提供されているライブラリ](#提供されているライブラリ)
2. [ローカル開発での依存追加](#ローカル開発での使用)
3. [Swift Package Managerでの依存追加（リモート）](#swift-package-managerでの使用リモート)
4. [Swiftから使用](#swiftから使用)
5. [リリース手順](#リリース手順)
6. [トラブルシューティング](#トラブルシューティング)

---

## 提供されているライブラリ

このプロジェクトでは、以下のXCFrameworkを提供しています：

- **Shared** - すべての機能を含む統合ライブラリ

**Sharedモジュールに含まれるもの：**
- 依存性注入（Koin）の初期化などアプリ全体の基盤
- Homeなどのフィーチャーモジュール
- その他すべてのKMPライブラリの機能

**重要**: **Sharedのみ**を追加すれば、すべてのViewModelにアクセスすることができるためUIの作成のみになります。

---

## ローカル開発での使用

開発中やテスト時には、ローカルでビルドしたXCFrameworkを使用できます。

### 1. XCFrameworkをビルド

```bash
./gradlew :shared:assembleSharedReleaseXCFramework --no-configuration-cache
```

ビルドされたファイル:
- `shared/build/XCFrameworks/release/Shared.xcframework`
- `shared/build/outputs/Shared.xcframework.zip`
- `shared/build/outputs/checksum.txt`

### 2. iOSプロジェクトに追加

1. Xcodeで対象のiOSプロジェクトを開く
2. プロジェクトナビゲーターでプロジェクトファイルを選択
3. ターゲットを選択 → `General` タブ
4. `Frameworks, Libraries, and Embedded Content` セクションで `+` をクリック
5. `Add Other...` → `Add Files...` を選択
6. `shared/build/XCFrameworks/release/Shared.xcframework` を選択
7. `Embed & Sign` に設定

---

## Swift Package Managerでの使用（リモート）

GitHubリリースからXCFrameworkを取得して使用します。

### 前提条件

- リポジトリにタグ（例: `v1.1.1`）が作成されている
- GitHub Releaseが公開されている
- `Package.swift` が最新のchecksumで更新されている

### 1. Xcodeでパッケージを追加

1. Xcodeでプロジェクトを開く
2. File → Add Package Dependencies
3. 右上の検索バーに入力: `https://github.com/kei-1111/kmp-sample-library`
4. バージョンを選択（例: `1.3.1`）
5. `Add Package` をクリック
6. **ライブラリを選択**: `Shared`
7. ターゲットに追加

---

## Swiftから使用

```swift
import Shared

// アプリのエントリーポイント
@main
struct YourApp: App {
    init() {
        // Koinを初期化（必須）
        KoinKt.initKoin(appContext: nil)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}


import Shared

// ViewModelの値をObserve（:feature:homeのHomeViewModel）
@MainActor
class HomeViewModelObserver: ObservableObject {
    var viewModel: HomeViewModel = ViewModelProvider.shared.provideHomeViewModel()
    @Published var state: HomeUiState = HomeUiStateInit()

    func startObserving() async {
        for await newState in viewModel.state {
            self.state = newState
        }
    }
}


import Shared

// Observeした値を使用
struct HomeScreen: View {
    @StateObject private var observer = HomeViewModelObserver()

    var body: some View {
        VStack(spacing: 20) {
            switch observer.state {
            case is HomeUiStateInit:
                Text("Welcome to Home Screen")

            case is HomeUiStateLoading:
                Text("Loading...")

            case let stable as HomeUiStateStable:
                Text("Stable State")

            case let error as HomeUiStateError:
                Text("Error")

            default:
                Text("Unknown State")
            }
        }
        .task { await observer.startObserving() }
    }
}
```

---

## リリース手順

新しいバージョンをリリースする際の手順です。**完全自動化されているため、非常にシンプルです。**

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

**これだけです！** あとはGitHub Actionsが自動的にすべて実行します。

### 4. GitHub Actionsが自動実行（完全自動化）

タグがプッシュされると、GitHub Actions（`.github/workflows/ios-release.yml`）が自動的に:

1. ✅ XCFramework（Shared）をビルド
2. ✅ チェックサムを計算
3. ✅ **Package.swiftを自動更新**（正しいチェックサム値で）
4. ✅ 変更をコミット＆プッシュ
5. ✅ タグを最新のコミットに移動
6. ✅ GitHub Releaseを作成
7. ✅ XCFrameworkのzipファイルをリリースに添付

**手動でXCFrameworkをビルドしたり、Package.swiftを更新したりする必要は一切ありません。**

### 5. リリースを確認

https://github.com/kei-1111/kmp-sample-library/releases で新しいリリースが作成されていることを確認

---

## トラブルシューティング

### ビルドエラー: `xcrun: error: unable to find utility "xcodebuild"`

Xcodeコマンドラインツールが正しく設定されていません。

```bash
# Xcodeのパスを確認
xcode-select -p

# CommandLineToolsを指している場合は、Xcodeアプリに切り替え
sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer

# または最新のXcodeを使用
sudo xcode-select --switch /Applications/Xcode-16.4.0.app/Contents/Developer
```

### Xcodeでパッケージが見つからない

1. File → Packages → Reset Package Caches を実行
2. プロジェクトをクリーン（Command + Shift + K）
3. Derived Dataを削除

### ローカルXCFrameworkが更新されない

XCFrameworkをビルドし直した後:
1. Xcodeを終了
2. XCFrameworkを再ビルド: `./gradlew :shard:assembleSharedReleaseXCFramework`
3. Xcodeで該当のXCFrameworkを削除して再度追加
4. Derived Dataを削除
5. Xcodeを再起動

### Koin初期化エラー

`Shared` モジュールが正しくインポートされていない、またはKoinの初期化が実行されていない可能性があります。

- アプリ起動時（`App`の`init()`内）で `KoinKt.initKoin(appContext: nil)` を呼び出しているか確認
- `import Shared` が正しくインポートされているか確認

### フィーチャーモジュールのクラスが見つからない

Sharedモジュールに含まれるべきクラス（Homeモジュールなど）が見つからない場合:

1. `shared/build.gradle.kts`で該当モジュールが`api`で依存されているか確認
2. `afterEvaluate`ブロックで`export()`が設定されているか確認
3. XCFrameworkを再ビルドして、Xcodeで更新

```kotlin
// shared/build.gradle.kts
commonMain.dependencies {
    api(projects.feature.home)  // apiで依存
}

afterEvaluate {
    kotlin.targets.withType<KotlinNativeTarget>().configureEach {
        binaries.withType<Framework>().configureEach {
            export(projects.feature.home)  // iOSに公開
        }
    }
}
```

---

## 参考情報

- [Kotlin Multiplatform公式ドキュメント](https://kotlinlang.org/docs/multiplatform.html)
- [XCFrameworkについて](https://developer.apple.com/documentation/xcode/creating-a-multi-platform-binary-framework-bundle)
- [Swift Package Manager](https://swift.org/package-manager/)