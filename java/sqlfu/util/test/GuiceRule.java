package sqlfu.util.test;

import com.google.inject.*;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class GuiceRule implements TestRule {

  private final Object testObject;
  private final Class<? extends Module> moduleClass;

  public GuiceRule(final Object testObject, final Class<? extends Module> moduleClass) {
    this.testObject = testObject;
    this.moduleClass = moduleClass;
  }

  @Override
  public Statement apply(final Statement statement, final Description description) {
    return new GuiceRuleStatement(statement, description);
  }

  private final class GuiceRuleStatement extends Statement {

    private final Statement baseStatement;
    private final Description description;

    public GuiceRuleStatement(final Statement baseStatement, final Description description) {
      this.baseStatement = baseStatement;
      this.description = description;
    }

    @Override
    public void evaluate() throws Throwable {
      createInjector().injectMembers(testObject);
      baseStatement.evaluate();
    }

    private Injector createInjector() {
      final Module module;
      try {
        module = moduleClass.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }

      return Guice.createInjector(module, new GuiceRuleModule());
    }

    private final class GuiceRuleModule extends AbstractModule {

      @Provides
      Description provideDescription() {
        return description;
      }
    }
  }
}
