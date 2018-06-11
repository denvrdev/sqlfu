package(
    default_visibility = ["//visibility:private"],
    default_testonly = 1,
)

load("//tools/build_defs:java_individual_tests.bzl", "java_individual_tests")

java_individual_tests(
    name = "tests",
    srcs = glob(["*Test.java"]),
    resources = [
        "test.sqlite",
    ],
    deps = [
        "//java/denvr/testing:AssertThrows",
        "//java/denvr/testing:FileSystemUtil",
        "//java/denvr/testing:InMemoryFileSystemProvider",
        "//java/sqlfu",
        "@com_google_guava",
        "@com_google_guiceberry",
        "@com_google_truth",
        "@javax_inject",
        "@junit",
    ],
)
