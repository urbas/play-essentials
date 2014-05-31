package si.urbas.pless.users;

import play.data.Form;
import si.urbas.pless.PlessService;
import si.urbas.pless.util.ServiceLoader;

import static play.data.Form.form;
import static si.urbas.pless.users.UserRepository.getUserRepository;

public class UserAccountService implements PlessService {
  public static final String CONFIG_USER_ACCOUNT_SERVICE = "pless.userAccountService";

  public Form<?> getAccountUpdateForm() {
    return form(SignupData.class);
  }

  public static UserAccountService getUserAccountService() {
    return UserAccountServiceLoader.INSTANCE.getService();
  }

  public PlessUser createUpdatedUser(Form<?> updateAccountForm) {
    SignupData signupData = (SignupData) updateAccountForm.get();
    return getUserRepository().createUser(signupData.email, signupData.username, signupData.password);
  }

  static class UserAccountServiceLoader {
    public static final ServiceLoader<UserAccountService> INSTANCE = new ServiceLoader<>(CONFIG_USER_ACCOUNT_SERVICE, new UserAccountService());
  }
}
