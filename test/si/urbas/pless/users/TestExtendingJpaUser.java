package si.urbas.pless.users;

import javax.persistence.Entity;

import static si.urbas.pless.util.StringUtils.isNullOrEmpty;

@Entity
public class TestExtendingJpaUser extends JpaPlessUser {

  public String testColumn;

  public TestExtendingJpaUser() {
  }

  public TestExtendingJpaUser(String email, String password, String testValue) {
    super(email, password);
    testColumn = testValue;
  }

  @Override
  public String validateForPersist() {
    return isNullOrEmpty(testColumn) ? "The testColumn must not be null or empty." : super.validateForPersist();
  }
}
