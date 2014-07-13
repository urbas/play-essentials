package si.urbas.pless.jpasample;

// SNIPPET: testPersistenceUnit
import si.urbas.pless.test.PlayJpaControllerTest;

@SuppressWarnings("UnusedDeclaration")
public class MyControllerTest extends PlayJpaControllerTest {

  @Override
  protected String getTestPersistenceUnit() {
    return "pless.jpasample.testPersistenceUnit";
  }
}
// ENDSNIPPET: testPersistenceUnit
