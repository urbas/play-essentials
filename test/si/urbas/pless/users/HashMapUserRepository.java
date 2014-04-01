package si.urbas.pless.users;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapUserRepository implements UserRepository {

  private final HashMap<String, User> emailToUserMap = new HashMap<>();
  private final HashMap<Long, User> idToUserMap = new HashMap<>();
  private long maxId = 0;

  @Override
  public synchronized User findUserByEmail(String email) {
    User user = emailToUserMap.get(email);
    if (user == null) {
      throw new NoResultException();
    }
    return user;
  }

  @Override
  public synchronized List<User> getAllUsers() {
    return new ArrayList<>(emailToUserMap.values());
  }

  @Override
  public synchronized void persistUser(String email, String password) {
    User newUser = new JpaUser(email, password).withId(++maxId);
    emailToUserMap.put(email, newUser);
    idToUserMap.put(newUser.getId(), newUser);
  }

  @Override
  public synchronized boolean activateUser(String userEmail, String activationCode) {
    User user = findUserByEmail(userEmail);
    if (user.getActivationCode().equals(activationCode)) {
      user.setActivated(true);
      return true;
    }
    return false;
  }

  @Override
  public synchronized boolean delete(String userEmail) {
    User removedUser = emailToUserMap.remove(userEmail);
    if (removedUser != null) {
      idToUserMap.remove(removedUser.getId());
    }
    return removedUser != null;
  }

  @Override
  public synchronized User findUserById(long userId) {
    // TODO: Throw an exception similar to find by email.
    return idToUserMap.get(userId);
  }
}
