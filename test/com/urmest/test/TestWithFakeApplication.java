package com.urmest.test;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

import play.db.jpa.JPA;

public abstract class TestWithFakeApplication {
  protected FakeApplicationBuilder applicationBuilder = new FakeApplicationBuilder();

  @Before
  public void setUp() {
    applicationBuilder.start();
  }

  @After
  public void tearDown() {
    applicationBuilder.close();
  }

  public static EntityManager getEm() {
    return JPA.em("default");
  }
}