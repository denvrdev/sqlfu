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
        "//java/sqlfu",
        "//java/sqlfu/util/test:AssertThrows",
        "//java/sqlfu/util/test:GuiceRule",
        "//java/sqlfu/util/test:InMemoryFileSystemProvider",
        "@com_google_guava",
        "@com_google_guiceberry",
        "@com_google_truth",
        "@javax_inject",
        "@junit",
    ],
)
