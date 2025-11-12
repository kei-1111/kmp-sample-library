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
            url: "https://github.com/kei-1111/kmp-sample-library/releases/download/v1.5.0/Shared.xcframework.zip",
            checksum: "7921ae303fe58fdd0dc268de51ef06d1b982e2557fcd2facc4b9240d0263167e"
        )
    ]
)
