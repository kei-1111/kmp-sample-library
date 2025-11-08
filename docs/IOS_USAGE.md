# iOSでの使用方法

このドキュメントでは、KMP Sample LibraryをiOSプロジェクトで使用する方法を説明します。

## 目次

1. [提供されているライブラリ](#提供されているライブラリ)
2. [ローカル開発での使用](#ローカル開発での使用)
3. [Swift Package Managerでの使用（リモート）](#swift-package-managerでの使用リモート)
4. [リリース手順](#リリース手順)
5. [トラブルシューティング](#トラブルシューティング)

---

## 提供されているライブラリ

このプロジェクトでは、以下の2つのXCFrameworkを提供しています：

- **Shared** - Koinの初期化などアプリケーションのエントリーポイント（必須）
- **Home** - ホーム画面を形作るためのフィーチャーモジュール

**重要**: アプリケーションを構築するには、通常 **Shared** と **Home** の両方が必要です。
- **Shared** は依存性注入（Koin）の初期化などアプリ全体の基盤を提供
- **Home** はホーム画面のUI/ビジネスロジックを提供

---

## ローカル開発での使用

開発中やテスト時には、ローカルでビルドしたXCFrameworkを使用できます。

### 1. XCFrameworkをビルド

#### 両方を一度にビルド（推奨）

```bash
./gradlew :shared:packageXCFramework :feature:home:packageXCFramework
```

#### 個別にビルド

Sharedをビルド:
```bash
./gradlew :shared:packageXCFramework
```

ビルドされたファイル:
- `shared/build/XCFrameworks/release/Shared.xcframework`
- `shared/build/outputs/Shared.xcframework.zip`
- `shared/build/outputs/checksum.txt`

Homeをビルド:
```bash
./gradlew :feature:home:packageXCFramework
```

ビルドされたファイル:
- `feature/home/build/XCFrameworks/release/Home.xcframework`
- `feature/home/build/outputs/Home.xcframework.zip`
- `feature/home/build/outputs/checksum.txt`

### 2. iOSプロジェクトに追加

**両方のXCFrameworkを追加する必要があります：**

1. Xcodeで対象のiOSプロジェクトを開く
2. プロジェクトナビゲーターでプロジェクトファイルを選択
3. ターゲットを選択 → `General` タブ
4. `Frameworks, Libraries, and Embedded Content` セクションで `+` をクリック
5. `Add Other...` → `Add Files...` を選択
6. まず `shared/build/XCFrameworks/release/Shared.xcframework` を選択
7. `Embed & Sign` に設定
8. 再度 `+` をクリックして、`feature/home/build/XCFrameworks/release/Home.xcframework` を追加
9. こちらも `Embed & Sign` に設定

### 3. Swiftから使用

```swift
import Shared
import Home

// アプリのエントリーポイント
@main
struct YourApp: App {
    init() {
        // Sharedを使ってKoinを初期化（必須）
        KoinKt.initKoin(appContext: nil)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

// ContentViewなどでHomeモジュールを使用
struct ContentView: View {
    var body: some View {
        // Homeモジュールの画面やコンポーネントを使用
        Text("Home Screen")
    }
}
```

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
4. バージョンを選択（例: `1.1.1`）
5. `Add Package` をクリック
6. **両方のライブラリを選択**: `Shared` と `Home`
7. ターゲットに追加

### 2. Swiftから使用

```swift
import Shared
import Home

// Sharedを使ってKoinを初期化
@main
struct YourApp: App {
    init() {
        KoinKt.initKoin(appContext: nil)
    }

    var body: some Scene {
        WindowGroup {
            // Homeモジュールを使用
            ContentView()
        }
    }
}
```

---

## リリース手順

新しいバージョンをリリースする際の手順です。

### 1. XCFrameworkをビルド

```bash
./gradlew :shared:packageXCFramework :feature:home:packageXCFramework
```

### 2. Package.swiftを更新

```bash
# 自動更新スクリプトを使用
./scripts/update-package-swift.sh 1.2.0

# または手動で以下を更新:
# - url: バージョンタグを新しいバージョンに変更
# - checksum: ビルドしたXCFrameworkのchecksumに更新
#   - Shared: shared/build/outputs/checksum.txt
#   - Home: feature/home/build/outputs/checksum.txt
```

### 3. 変更をコミットしてタグを作成

```bash
# Package.swiftの変更を確認
git diff Package.swift

# コミット
git add Package.swift
git commit -m "chore: Package.swiftをv1.2.0に更新"

# タグを作成
git tag v1.2.0

# プッシュ（タグも含む）
git push origin main --tags
```

### 4. GitHub Actionsが自動実行

タグがプッシュされると、GitHub Actions（`.github/workflows/release.yml`）が自動的に:
1. SharedとHomeのXCFrameworkをビルド
2. GitHub Releaseを作成
3. 両方のXCFrameworkのzipファイルを添付

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
2. 両方のXCFrameworkを再ビルド
3. Xcodeで該当のXCFrameworkを削除して再度追加
4. Derived Dataを削除
5. Xcodeを再起動

### Koin初期化エラー

`Shared` モジュールが正しくインポートされていない、またはKoinの初期化が実行されていない可能性があります。

- アプリ起動時（`App`の`init()`内）で `KoinKt.initKoin(appContext: nil)` を呼び出しているか確認
- `import Shared` が正しくインポートされているか確認

### Homeモジュールでクラスが見つからない

両方のXCFrameworkがプロジェクトに追加されているか確認してください：
- `Shared.xcframework` - Koin初期化などの基盤
- `Home.xcframework` - ホーム画面のUI/ロジック

両方が必要です。

---

## 参考情報

- [Kotlin Multiplatform公式ドキュメント](https://kotlinlang.org/docs/multiplatform.html)
- [XCFrameworkについて](https://developer.apple.com/documentation/xcode/creating-a-multi-platform-binary-framework-bundle)
- [Swift Package Manager](https://swift.org/package-manager/)