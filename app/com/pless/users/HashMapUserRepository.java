package com.pless.users;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.NoResultException;

import com.pless.authentication.SaltedHashedPassword;

public class HashMapUserRepository implements UserRepository {

  private static final ConcurrentHashMap<String,User> usersMap = new ConcurrentHashMap<>();
  private static final AtomicLong maxUserId = new AtomicLong(0);

  @Override
  public User findUserByEmail(String email) {
    User foundUser = usersMap.get(email);
    if (foundUser == null) {
      throw new NoResultException();
    }
    return foundUser;
  }

  @Override
  public List<User> getAllUsers() {
    return new ArrayList<>(usersMap.values());
  }

  @Override
  public void persistUser(String email, String password) {
    long newUserId = maxUserId.addAndGet(1);
    SaltedHashedPassword saltedPassword = new SaltedHashedPassword(password);
    User newUser = new JpaUser(email, saltedPassword).withId(newUserId);
    User alreadyPresentUser = usersMap.putIfAbsent(email, newUser);
    if (alreadyPresentUser != null) {
      
    }
  }

  public void clear() {
    maxUserId.set(0);
    usersMap.clear();
  }

}
