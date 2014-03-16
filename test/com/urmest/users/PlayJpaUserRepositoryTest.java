package com.urmest.users;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.*;

public class PlayJpaUserRepositoryTest {
  private UserRepository userRepository;
  private EntityManager entityManager;
  private TypedQuery<JpaUser> query;
  private ArrayList<JpaUser> allUsers;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    entityManager = mock(EntityManager.class);
    query = mock(TypedQuery.class);
    when(entityManager.createNamedQuery(JpaUser.QUERY_GET_ALL)).thenReturn(query);
    allUsers = new ArrayList<JpaUser>();
    when(query.getResultList()).thenReturn(allUsers);
    userRepository = new TestPlayUserRepository(entityManager);
  }
  
  @Test
  public void getAllUsers_MUST_use_the_named_query() throws Exception {
    List<User> allUsers = userRepository.getAllUsers();
    Assert.assertEquals(this.allUsers, allUsers);
  }
}
