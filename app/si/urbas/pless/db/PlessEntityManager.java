package si.urbas.pless.db;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

import si.urbas.pless.ConfigurationException;
import si.urbas.pless.util.*;

public class PlessEntityManager {

  public static final String CONFIG_ENTITY_MANAGER_PROVIDER = "pless.entityManagerFactory";

  public static EntityManager getEntityManager() {
    return Singletons.ENTITY_MANAGER_FACTORY
      .createInstance(getConfigurationSource());
  }

  private static final class Singletons {
    public static final Factory<EntityManager> ENTITY_MANAGER_FACTORY = new CachingFactory<>(CONFIG_ENTITY_MANAGER_PROVIDER, new PlayDefaultEntityManagerFactory());
  }

  private static final class PlayDefaultEntityManagerFactory implements Factory<EntityManager> {
    @Override
    public EntityManager createInstance(ConfigurationSource configurationSource) {
      try {
        return JPA.em();
      } catch (Exception e) {
        throw new ConfigurationException("Could not create an entity manager." +
          " Have you configured the JPA persistence? Create a 'persistence.xml' file and put it into the " +
          "'cont/META-INF' folder.", e);
      }
    }
  }

}
