package com.pless.authentication;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.pless.test.PlessControllerTest;


public class PlayCacheSessionStorageTest extends PlessControllerTest {
  private static final String FOO = "foo";
  private static final String BAR = "bar";
  private static final int ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;
  private final PlayCacheSessionStorage cachedServerSessionStorage = new PlayCacheSessionStorage();

  @Test
  @Ignore
  public void get_MUST_return_the_stored_value() throws Exception {
    cachedServerSessionStorage.put(FOO, BAR, ONE_HOUR_IN_MILLIS);
    assertEquals(BAR, cachedServerSessionStorage.get(FOO));
  }
  
  @Test
  @Ignore
  public void get_MUST_return_null_AFTER_the_stored_value_was_removed() throws Exception {
    cachedServerSessionStorage.put(FOO, BAR, ONE_HOUR_IN_MILLIS);
    cachedServerSessionStorage.remove(FOO);
    assertEquals(null, cachedServerSessionStorage.get(FOO));
  }
}
