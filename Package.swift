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
            url: "https://github.com/kei-1111/kmp-sample-library/releases/download/v1.4.1/Shared.xcframework.zip",
            checksum: "20579c659b72926794f4db3901ea6d67f3c36234aa041a57d0b08cf80e969274"
        )
    ]
)
