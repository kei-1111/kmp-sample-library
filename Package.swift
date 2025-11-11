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
            url: "https://github.com/kei-1111/kmp-sample-library/releases/download/v1.3.1/Shared.xcframework.zip",
            checksum: "2436b3362af74530fceb724f47f9ae071189c8b4070a68155219ddec4562396e"
        )
    ]
)
