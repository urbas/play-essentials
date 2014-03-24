package com.pless.users;

import java.util.List;

public interface UserRepository {

  User findUserByEmail(String email);

  List<User> getAllUsers();

  void persistUser(String email, String password);

  boolean activateUser(String userEmail, String activationCode);
}