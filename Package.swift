// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "KmpSampleLibrary",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "Shared",
            targets: ["Shared"]
        )
    ],
    targets: [
        .binaryTarget(
            name: "Shared",
            url: "https://github.com/kei-1111/kmp-sample-library/releases/download/v1.4.3/Shared.xcframework.zip",
            checksum: "843703f00f74d57355cad40e9e40d66931152dfe08edbb702fa7b77895037215"
        )
    ]
)
