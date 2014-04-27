package si.urbas.pless.users;

import javax.persistence.Entity;

import static si.urbas.pless.util.StringUtils.isNullOrEmpty;

@Entity
public class TestExtendingJpaUser extends JpaPlessUser {

  public String testColumn;

  @SuppressWarnings("UnusedDeclaration")
  public TestExtendingJpaUser() {
  }

  public TestExtendingJpaUser(String email, String username, String password, String testValue) {
    super(email, username, password);
    testColumn = testValue;
  }

  @Override
  public String validateForPersist() {
    return isNullOrEmpty(testColumn) ? "The testColumn must not be null or empty." : super.validateForPersist();
  }
}
