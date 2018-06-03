workspace(name = "sqlfu")

load("@bazel_tools//tools/build_defs/repo:java.bzl", "java_import_external")

java_import_external(
    name = "com_google_guava",
    licenses = ["notice"],
    jar_urls = [
        "http://central.maven.org/maven2/com/google/guava/guava/25.1-jre/guava-25.1-jre.jar",
    ],
    jar_sha256 = "6db0c3a244c397429c2e362ea2837c3622d5b68bb95105d37c21c36e5bc70abf",
    srcjar_urls = [
        "http://central.maven.org/maven2/com/google/guava/guava/25.1-jre/guava-25.1-jre-sources.jar",
    ],
    srcjar_sha256 = "b7ffb578b2bd6445c958356e308d1c46c9ea6fb868fc9444bc8bda3a41875a1b",
    deps = [
        "@com_google_code_findbugs_jsr305",
        "@com_google_errorprone_error_prone_annotations",
    ],
)

java_import_external(
    name = "com_google_code_findbugs_jsr305",
    licenses = ["notice"],
    jar_urls = [
        "http://central.maven.org/maven2/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar",
    ],
    jar_sha256 = "766ad2a0783f2687962c8ad74ceecc38a28b9f72a2d085ee438b7813e928d0c7",
    srcjar_urls = [
        "http://central.maven.org/maven2/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2-sources.jar",
    ],
    srcjar_sha256 = "1c9e85e272d0708c6a591dc74828c71603053b48cc75ae83cce56912a2aa063b",
)

java_import_external(
    name = "com_google_errorprone_error_prone_annotations",
    licenses = ["notice"],
    jar_urls = [
        "http://central.maven.org/maven2/com/google/errorprone/error_prone_annotations/2.3.1/error_prone_annotations-2.3.1.jar",
    ],
    jar_sha256 = "10a5949aa0f95c8de4fd47edfe20534d2acefd8c224f8afea1f607e112816120",
    srcjar_urls = [
        "http://central.maven.org/maven2/com/google/errorprone/error_prone_annotations/2.3.1/error_prone_annotations-2.3.1-sources.jar",
    ],
    srcjar_sha256 = "0fe3db0b12e624afd1dbeba85421fa58c362f9caf55f1869d7683b8744c53616",
)

java_import_external(
    name = "junit",
    licenses = ["reciprocal"],
    testonly_ = True,
    jar_urls = [
        "http://central.maven.org/maven2/junit/junit/4.12/junit-4.12.jar",
    ],
    jar_sha256 = "59721f0805e223d84b90677887d9ff567dc534d7c502ca903c0c2b17f05c116a",
    srcjar_urls = [
        "http://central.maven.org/maven2/junit/junit/4.12/junit-4.12-sources.jar",
    ],
    srcjar_sha256 = "9f43fea92033ad82bcad2ae44cec5c82abc9d6ee4b095cab921d11ead98bf2ff",
)

java_import_external(
    name = "com_google_jimfs",
    licenses = ["notice"],
    testonly_ = True,
    jar_urls = [
        "http://central.maven.org/maven2/com/google/jimfs/jimfs/1.1/jimfs-1.1.jar",
    ],
    jar_sha256 = "c4828e28d7c0a930af9387510b3bada7daa5c04d7c25a75c7b8b081f1c257ddd",
    srcjar_urls = [
        "http://central.maven.org/maven2/com/google/jimfs/jimfs/1.1/jimfs-1.1-sources.jar",
    ],
    srcjar_sha256 = "adebb53450d2313d6927db5155c9be85336109e8f8c0af106ec2a30bc37e64ce",
)

java_import_external(
    name = "com_google_truth",
    licenses = ["notice"],
    testonly_ = True,
    jar_urls = [
        "http://central.maven.org/maven2/com/google/truth/truth/0.40/truth-0.40.jar",
    ],
    jar_sha256 = "4868998229b155dcbe68416803975d491704f7cdb6847346cf9a49f65067d6fd",
    srcjar_urls = [
        "http://central.maven.org/maven2/com/google/truth/truth/0.40/truth-0.40-sources.jar",
    ],
    srcjar_sha256 = "294e40be4ecbc8d7c25e6887774fcb004f178ea1dfa4988cfa7f2bc35516cf21",
)
