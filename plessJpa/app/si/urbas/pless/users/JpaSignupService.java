package si.urbas.pless.users;

import play.data.Form;

@SuppressWarnings("UnusedDeclaration")
public class JpaSignupService extends SignupService {

  @Override
  public PlessUser createUser(Form<?> signupForm) {
      SignupData signupData = (SignupData) signupForm.get();
      return new JpaPlessUser(signupData.email, signupData.username, signupData.password);
  }

}
