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
            checksum: "SHARED_CHECKSUM_PLACEHOLDER"
        ),
        .binaryTarget(
            name: "Home",
            url: "https://github.com/kei-1111/kmp-sample-library/releases/download/v1.2.0/Home.xcframework.zip",
            checksum: "HOME_CHECKSUM_PLACEHOLDER"
        )
    ]
)