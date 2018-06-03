# Skylark macros to assist with writing JUnit tests

def java_individual_tests(name, srcs, **kwargs):
  """
  Creates one java_test() rule per source file.

  Args:
    name
      a string whose value is the name assigned to this rule; it is not used by this method and
      has no effects on the generated build targets.
    srcs
      a list of strings, the source files that define the Java tests;
      each source file will be converted into a java_test build target.
    kwargs
      the arguments for specify to the java_test build target (other that "name" and "srcs").
  """

  for src in srcs:
    if not src.endswith(".java"):
      fail("file name does not end with '.java': {}".format(src), "srcs")

    name = src[:-5]
    native.java_test(name=name, srcs=[src], **kwargs)
