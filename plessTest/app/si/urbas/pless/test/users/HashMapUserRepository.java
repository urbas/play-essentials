package si.urbas.pless.test.users;

import si.urbas.pless.authentication.SaltedHashedPassword;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapUserRepository extends UserRepository {

  private final HashMap<String, PlessUser> emailToUserMap = new HashMap<>();
  private final HashMap<Long, PlessUser> idToUserMap = new HashMap<>();
  private long maxId = 0;

  public synchronized PlessUser getUser(String email) {return emailToUserMap.get(email);}

  @Override
  public synchronized PlessUser findUserByEmail(String email) {
    PlessUser user = getUser(email);
    if (user == null) {
      throw new IllegalArgumentException("Could not find user with the email '" + email + "'");
    }
    return user;
  }


  @Override
  public synchronized List<PlessUser> getAllUsers() {
    return new ArrayList<>(emailToUserMap.values());
  }

  @Override
  public synchronized void persistUser(String email, String username, String password) {
    persistUser(createUser(email, username, password));
  }

  @Override
  public synchronized void persistUser(PlessUser user) {
    String validationError = user.validateForPersist();
    if (validationError == null) {
      if (user.getId() != 0) {
        throw new RuntimeException("Cannot persist the user '"+user+"'. This user already has a non-zero ID, which means it's already persisted.");
      }
      user.setId(++maxId);
      emailToUserMap.put(user.getEmail(), user);
      idToUserMap.put(user.getId(), user);
    } else {
      throw new RuntimeException("Cannot persist the user '" + user + "'. Validation error: " + validationError);
    }
  }

  @Override
  public synchronized boolean activateUser(String userEmail, String activationCode) {
    PlessUser user = getUser(userEmail);
    if (user != null && user.getActivationCode().equals(activationCode)) {
      user.setActivated(true);
      return true;
    }
    return false;
  }

  @Override
  public synchronized boolean delete(String userEmail) {
    PlessUser removedUser = emailToUserMap.remove(userEmail);
    if (removedUser != null) {
      idToUserMap.remove(removedUser.getId());
    }
    return removedUser != null;
  }

  @Override
  public synchronized PlessUser findUserById(long userId) {
    PlessUser user = idToUserMap.get(userId);
    if (user == null) {
      throw new IllegalArgumentException("Could not find user with the id '" + userId + "'");
    }
    return user;
  }

  @Override
  public synchronized boolean setUsername(long userId, String username) {
    PlessUser user = idToUserMap.get(userId);
    if (user == null) {
      return false;
    }
    user.setUsername(username);
    return true;
  }

  @Override
  public PlessUser createUser(String email, String username, String password) {
    return new PlessUser(0, email, username, new SaltedHashedPassword(password));
  }
}
