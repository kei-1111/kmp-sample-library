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
        ),
        .library(
            name: "Home",
            targets: ["Home"]
        )
    ],
    targets: [
        .binaryTarget(
            name: "Shared",
            url: "https://github.com/kei-1111/kmp-sample-library/releases/download/v1.2.0/Shared.xcframework.zip",
            checksum: "5994c41664cbf3fc67e8528e45d9e56f41b3f0a8857ad19eb30e960c296afda0"
        ),
        .binaryTarget(
            name: "Home",
            url: "https://github.com/kei-1111/kmp-sample-library/releases/download/v1.2.0/Home.xcframework.zip",
            checksum: "13e92745e7a92d2e4820d149f31287f41c1d45fe1bb081166fd162a59142d006"
        )
    ]
)
