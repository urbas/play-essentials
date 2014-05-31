package si.urbas.pless.test.users;

import si.urbas.pless.authentication.SaltedHashedPassword;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapUserRepository extends UserRepository {

  private final HashMap<String, PlessUser> emailToUserMap = new HashMap<>();
  private final HashMap<Long, PlessUser> idToUserMap = new HashMap<>();
  private final HashMap<String, PlessUser> usernameToUserMap = new HashMap<>();
  private long maxId = 0;

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
  public synchronized void persistUser(PlessUser user) {
    String validationError = user.validateForPersist();
    if (validationError == null) {
      if (user.getId() != 0) {
        throwPersistUserException("This user already has a non-zero ID, which means it's already persisted.", user);
      }
      user.setId(++maxId);
      addUser(user);
    } else {
      throwPersistUserException("Validation error: " + validationError, user);
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
    return removeUser(getUser(userEmail)) != null;
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
    assertNoOtherUserHasGivenUsername(username, user);
    user.setUsername(username);
    return true;
  }

  @Override
  public void mergeUser(PlessUser updatedUser) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PlessUser createUser(String email, String username, String password) {
    return new PlessUser(0, email, username, new SaltedHashedPassword(password));
  }

  private synchronized PlessUser getUser(String email) {return emailToUserMap.get(email);}

  private PlessUser addUser(PlessUser user) {
    if (StringUtils.isNullOrEmpty(user.getEmail())) {
      throwPersistUserException("The email cannot be 'null' or empty.", user);
    }
    if (usernameToUserMap.containsKey(user.getUsername())) {
      throwPersistUserException("Another user has the same username.", user);
    }
    if (emailToUserMap.containsKey(user.getEmail())) {
      throwPersistUserException("Another user has the same email.", user);
    }
    usernameToUserMap.put(user.getUsername(), user);
    emailToUserMap.put(user.getEmail(), user);
    return idToUserMap.put(user.getId(), user);
  }

  private PlessUser removeUser(PlessUser user) {
    if (user == null) {
      return null;
    }
    usernameToUserMap.remove(user.getUsername());
    emailToUserMap.remove(user.getEmail());
    return idToUserMap.remove(user.getId());
  }

  private void assertNoOtherUserHasGivenUsername(String username, PlessUser user) {
    PlessUser userWithSameUsername = usernameToUserMap.get(username);
    if (userWithSameUsername != null && userWithSameUsername != user) {
      throw new RuntimeException("Could not set the new username. Another user already has the given username.");
    }
  }
}
