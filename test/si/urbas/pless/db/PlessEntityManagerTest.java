package si.urbas.pless.db;

import org.junit.Test;
import si.urbas.pless.test.PlessTest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.db.PlessEntityManager.clearCachedEntityManagerFactory;
import static si.urbas.pless.db.PlessEntityManager.getEntityManager;
import static si.urbas.pless.test.TestEntityManagerFactory.getCurrentEntityManagerFactory;
import static si.urbas.pless.test.TestEntityManagerFactory.getEntityManagerFactoryConstructionCount;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessEntityManagerTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final PlessEntityManager plessEntityManager = new PlessEntityManager();

  @Test
  public void getEntityManager_MUST_return_the_configured_EntityManager() throws Exception {
    assertThat(
      getEntityManager(),
      is(sameInstance(getCurrentEntityManagerFactory().createInstance(getConfigurationSource())))
    );
  }

  @Test
  public void getEntityManager_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(getConfigurationSource().isProduction()).thenReturn(true);
    // Note: we have to clear the cache here because other test methods initialise the caching factory which is static.
    clearCachedEntityManagerFactory();
    getEntityManager();
    getEntityManager();
    assertEquals(
      1,
      getEntityManagerFactoryConstructionCount()
    );
  }

  @Test
  public void getEntityManager_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    getEntityManager();
    getEntityManager();
    assertEquals(
      2,
      getEntityManagerFactoryConstructionCount()
    );
  }
}
