package si.urbas.pless.jpasample;

import si.urbas.pless.test.PlayJpaControllerTest;

@SuppressWarnings("UnusedDeclaration")
// SNIPPET: testPersistenceUnit
public class MyControllerTest extends PlayJpaControllerTest {

  @Override
  protected String getTestPersistenceUnit() {
    return "pless.example.testPersistenceUnit";
  }
}
// ENDSNIPPET: testPersistenceUnit
