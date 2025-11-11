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
            url: "https://github.com/kei-1111/kmp-sample-library/releases/download/v1.4.2/Shared.xcframework.zip",
            checksum: "ddfce6981307c1aaf88a022a692b509cdda1040ce94661aeaa648fe0c2b02448"
        )
    ]
)
