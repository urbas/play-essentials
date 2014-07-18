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
    return user.clone();
  }

  @Override
  public PlessUser findUserByUsername(String username) {
    PlessUser plessUser = getUserByUsername(username);
    if (plessUser == null) {
      throw new IllegalArgumentException("Could not find user with the username '" + username + "'.");
    }
    return plessUser;
  }

  @Override
  public synchronized List<PlessUser> getAllUsers() {
    ArrayList<PlessUser> plessUsers = new ArrayList<>();
    for (PlessUser plessUser : emailToUserMap.values()) {
      plessUsers.add(plessUser);
    }
    return plessUsers;
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
    return user.clone();
  }

  @Override
  public synchronized void mergeUser(PlessUser userWithUpdatedFields) {
    PlessUser persistedUser = getUserById(userWithUpdatedFields.getId());
    if (persistedUser == null) {
      throw new RuntimeException("Could not apply changes to the user. The user with the ID '" + userWithUpdatedFields.getId() + "' was not persisted in this user repository.");
    }
    PlessUser newUser = userWithUpdatedFields.clone();
    newUser.setCreationDate(persistedUser.getCreationDate());
    removeUser(persistedUser);
    addUser(newUser);
  }

  @Override
  public PlessUser createUser(String email, String username, String password) {
    return new PlessUser(0, email, username, new SaltedHashedPassword(password));
  }

  private synchronized PlessUser getUserById(long updatedUserId) {return idToUserMap.get(updatedUserId);}

  private synchronized PlessUser getUser(String email) {return emailToUserMap.get(email);}

  private PlessUser getUserByUsername(String username) {return usernameToUserMap.get(username);}

  private PlessUser addUser(PlessUser newUser) {
    PlessUser clonedUser = newUser.clone();
    if (StringUtils.isNullOrEmpty(clonedUser.getEmail())) {
      throwPersistUserException("The email cannot be 'null' or empty.", clonedUser);
    }
    if (usernameToUserMap.containsKey(clonedUser.getUsername())) {
      throwPersistUserException("Another user has the same username.", clonedUser);
    }
    if (emailToUserMap.containsKey(clonedUser.getEmail())) {
      throwPersistUserException("Another user has the same email.", clonedUser);
    }
    usernameToUserMap.put(clonedUser.getUsername(), clonedUser);
    emailToUserMap.put(clonedUser.getEmail(), clonedUser);
    return idToUserMap.put(clonedUser.getId(), clonedUser);
  }

  private PlessUser removeUser(PlessUser user) {
    if (user == null) {
      return null;
    }
    usernameToUserMap.remove(user.getUsername());
    emailToUserMap.remove(user.getEmail());
    return idToUserMap.remove(user.getId());
  }

}
