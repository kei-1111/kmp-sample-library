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
            checksum: "9779c67a322d18743823d55272d6665e6b3bd3ae9a1036ac17dfcda1ba60c0e0"
        ),
        .binaryTarget(
            name: "Home",
            url: "https://github.com/kei-1111/kmp-sample-library/releases/download/v1.2.0/Home.xcframework.zip",
            checksum: "d534dd2bebc638d6c06fb6ad22bbf4f3a87d00fe79d13138db71b024539dcd3f"
        )
    ]
)
