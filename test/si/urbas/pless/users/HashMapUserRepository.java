package si.urbas.pless.users;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HashMapUserRepository implements UserRepository {

  private final HashMap<String, PlessUser> emailToUserMap = new HashMap<>();
  private final HashMap<Long, PlessUser> idToUserMap = new HashMap<>();
  private long maxId = 0;

  public PlessUser getUser(String email) {return emailToUserMap.get(email);}

  @Override
  public synchronized PlessUser findUserByEmail(String email) {
    PlessUser user = getUser(email);
    if (user == null) {
      throw new NoResultException();
    }
    return user;
  }


  @Override
  public synchronized List<PlessUser> getAllUsers() {
    return new ArrayList<>(emailToUserMap.values());
  }

  @Override
  public synchronized void persistUser(String email, String username, String password) {
    JpaPlessUser newUser = new JpaPlessUser(email, username, password).withId(++maxId);
    newUser.setCreationDate(new Date());
    persistUser(newUser);
  }

  @Override
  public void persistUser(PlessUser newUser) {
    emailToUserMap.put(newUser.getEmail(), newUser);
    idToUserMap.put(newUser.getId(), newUser);
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
      throw new NoResultException("Could not the user.");
    }
    return user;
  }
}
