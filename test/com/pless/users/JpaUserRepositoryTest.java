package com.pless.users;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.*;

public class JpaUserRepositoryTest {
  private UserRepository userRepository;
  private EntityManager entityManager;
  private TypedQuery<JpaUser> query;
  private ArrayList<JpaUser> allUsers;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    entityManager = mock(EntityManager.class);
    query = mock(TypedQuery.class);
    allUsers = new ArrayList<JpaUser>();
    
    when(entityManager.createNamedQuery(JpaUser.QUERY_GET_ALL)).thenReturn(query);
    when(query.getResultList()).thenReturn(allUsers);
    
    userRepository = new JpaUserRepository(entityManager);
  }
  
  @Test
  public void getAllUsers_MUST_use_the_named_query() throws Exception {
    List<User> allUsers = userRepository.getAllUsers();
    Assert.assertEquals(this.allUsers, allUsers);
  }
}
