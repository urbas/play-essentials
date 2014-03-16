package com.pless.users;

import java.util.List;

public interface UserRepository {

  public abstract User findUserByEmail(String email);

  public abstract List<User> getAllUsers();

  public abstract void persistUser(String name, String email, String password);

}