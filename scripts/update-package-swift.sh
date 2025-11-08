#!/bin/bash

# Package.swiftのchecksumとバージョンを更新するスクリプト

set -e

if [ -z "$1" ]; then
    echo "使用方法: $0 <version>"
    echo "例: $0 1.1.1"
    exit 1
fi

VERSION=$1
SHARED_CHECKSUM_FILE="shared/build/outputs/checksum.txt"
HOME_CHECKSUM_FILE="feature/home/build/outputs/checksum.txt"
PACKAGE_SWIFT="Package.swift"

# checksumファイルが存在するか確認
if [ ! -f "$SHARED_CHECKSUM_FILE" ]; then
    echo "❌ エラー: $SHARED_CHECKSUM_FILE が見つかりません"
    echo "先に './gradlew :shared:packageXCFramework' を実行してください"
    exit 1
fi

if [ ! -f "$HOME_CHECKSUM_FILE" ]; then
    echo "❌ エラー: $HOME_CHECKSUM_FILE が見つかりません"
    echo "先に './gradlew :feature:home:packageXCFramework' を実行してください"
    exit 1
fi

# checksumを読み込む
SHARED_CHECKSUM=$(cat "$SHARED_CHECKSUM_FILE")
HOME_CHECKSUM=$(cat "$HOME_CHECKSUM_FILE")

echo "📋 Version: $VERSION"
echo "🔑 Shared Checksum: $SHARED_CHECKSUM"
echo "🔑 Home Checksum: $HOME_CHECKSUM"

# Package.swiftを更新（複数のバイナリターゲットに対応）
# まずバージョンを更新
sed -i.bak \
    -e "s|/releases/download/v[0-9.]*|/releases/download/v${VERSION}|g" \
    "$PACKAGE_SWIFT"

# Sharedのchecksumを更新
sed -i.bak \
    -e "/name: \"Shared\"/,/checksum:/ s|checksum: \"[^\"]*\"|checksum: \"${SHARED_CHECKSUM}\"|" \
    "$PACKAGE_SWIFT"

# Homeのchecksumを更新
sed -i.bak \
    -e "/name: \"Home\"/,/checksum:/ s|checksum: \"[^\"]*\"|checksum: \"${HOME_CHECKSUM}\"|" \
    "$PACKAGE_SWIFT"

# バックアップファイルを削除
rm -f "${PACKAGE_SWIFT}.bak"

echo "✅ Package.swift を更新しました"
echo ""
echo "次のステップ:"
echo "1. 変更を確認: git diff Package.swift"
echo "2. コミット: git add Package.swift && git commit -m \"chore: Package.swiftをv${VERSION}に更新\""
echo "3. タグ作成: git tag v${VERSION}"
echo "4. プッシュ: git push origin main --tags"