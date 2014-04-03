package si.urbas.pless.users;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HashMapUserRepository implements UserRepository {

  private final HashMap<String, User> emailToUserMap = new HashMap<>();
  private final HashMap<Long, User> idToUserMap = new HashMap<>();
  private long maxId = 0;

  public User getUser(String email) {return emailToUserMap.get(email);}

  @Override
  public synchronized User findUserByEmail(String email) {
    User user = getUser(email);
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
    JpaUser newUser = new JpaUser(email, password).withId(++maxId);
    newUser.setCreationDate(new Date());
    emailToUserMap.put(email, newUser);
    idToUserMap.put(newUser.getId(), newUser);
  }

  @Override
  public synchronized boolean activateUser(String userEmail, String activationCode) {
    User user = getUser(userEmail);
    if (user != null && user.getActivationCode().equals(activationCode)) {
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

    User user = idToUserMap.get(userId);
    if (user == null) {
      throw new NoResultException("Could not the user.");
    }
    return user;
  }
}
