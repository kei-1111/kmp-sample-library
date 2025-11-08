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
            checksum: "6ad86ee6a0830e435c587271c5e62dc009d95058f70864986249d7395673341d"
        ),
        .binaryTarget(
            name: "Home",
            url: "https://github.com/kei-1111/kmp-sample-library/releases/download/v1.2.0/Home.xcframework.zip",
            checksum: "4c3330cc3d1005d446cff9ddd2bb8af24cc1a0c46ff7cdc7cd4444dbd9d54bb8"
        )
    ]
)
