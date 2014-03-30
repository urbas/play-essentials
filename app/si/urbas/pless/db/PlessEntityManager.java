package si.urbas.pless.db;

import play.db.jpa.JPA;
import si.urbas.pless.ConfigurationException;
import si.urbas.pless.util.CachingFactory;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

import javax.persistence.EntityManager;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessEntityManager {

  public static final String CONFIG_ENTITY_MANAGER_PROVIDER = "pless.entityManagerFactory";

  public static EntityManager getEntityManager() {
    return Singletons.ENTITY_MANAGER_FACTORY
      .createInstance(getConfigurationSource());
  }

  private static final class Singletons {
    public static final CachingFactory<EntityManager> ENTITY_MANAGER_FACTORY = new CachingFactory<>(CONFIG_ENTITY_MANAGER_PROVIDER, new PlayDefaultEntityManagerFactory());
  }

  private static final class PlayDefaultEntityManagerFactory implements Factory<EntityManager> {
    @Override
    public EntityManager createInstance(ConfigurationSource configurationSource) {
      try {
        return JPA.em();
      } catch (Exception e) {
        return throwJpaDescriptiveMisConfigurationException(e);
      }
    }
  }

  public static <T> T throwJpaDescriptiveMisConfigurationException(Throwable innerException) {
    throw new ConfigurationException(
      "Could not create an entity manager. " +
        "Have you configured JPA persistence? " +
        "Create a 'persistence.xml' file and put it into the 'cont/META-INF' folder. " +
        "See 'https://github.com/urbas/play-essentials/blob/master/conf/META-INF/persistence.xml' for an example. " +
        "You'll also have to add 'jpa.default=si.urbas.pless.defaultPersistenceUnit' into 'conf/application.conf'.'",
      innerException
    );
  }

  static void clearCachedEntityManagerFactory() {
    Singletons.ENTITY_MANAGER_FACTORY.clearCache();
  }

}
