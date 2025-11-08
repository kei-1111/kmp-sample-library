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
            checksum: "8d04686eec4f35f5f892fe031da4e162cbf11804eaf5211b471c788487ef3119"
        ),
        .binaryTarget(
            name: "Home",
            url: "https://github.com/kei-1111/kmp-sample-library/releases/download/v1.2.0/Home.xcframework.zip",
            checksum: "d3bfda8164a8c1bac407dbaf287dc0d326aedc1fc333f10d72720f80482beaae"
        )
    ]
)
