package(
    default_visibility = ["//visibility:private"],
    default_testonly = 1,
)

load("//tools/build_defs:java_individual_tests.bzl", "java_individual_tests")

java_individual_tests(
    name = "tests",
    srcs = glob(["*Test.java"]),
    deps = [
        "//java/sqlfu",
        "//java/sqlfu/util/test",
        "@com_google_jimfs",
        "@com_google_guava",
        "@com_google_truth",
        "@junit",
    ],
)
