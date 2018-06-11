package(
    default_visibility = ["//visibility:public"],
    default_testonly = 1,
)

java_library(
    name = "AssertThrows",
    srcs = ["AssertThrows.java"],
)

java_library(
    name = "GuiceRule",
    srcs = ["GuiceRule.java"],
    deps = [
        "@com_google_inject_guice",
        "@junit",
    ]
)

java_library(
    name = "InMemoryFileSystemProvider",
    srcs = ["InMemoryFileSystemProvider.java"],
    deps = [
        "@javax_inject",
        "@com_google_jimfs",
    ]
)

java_library(
    name = "FileSystemUtil",
    srcs = ["FileSystemUtil.java"],
    deps = [
        "@javax_inject",
    ]
)
