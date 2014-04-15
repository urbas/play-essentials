package si.urbas.pless.users;

import java.util.List;

public interface UserRepository {

  PlessUser findUserByEmail(String email);

  List<PlessUser> getAllUsers();

  void persistUser(String email, String password);

  boolean activateUser(String userEmail, String activationCode);

  boolean delete(String userEmail);

  PlessUser findUserById(long userId);
}