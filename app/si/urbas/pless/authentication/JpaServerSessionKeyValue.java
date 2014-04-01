package si.urbas.pless.authentication;

import java.util.Calendar;

import javax.persistence.*;

@Entity(name = "Session")
@NamedQueries({
  @NamedQuery(name = JpaServerSessionKeyValue.QUERY_DELETE_BY_KEY, query = "DELETE FROM Session s WHERE s.key = :key")
})

public class JpaServerSessionKeyValue {
  public static final String QUERY_DELETE_BY_KEY = "JpaServerSessionKeyValue.deleteByKey";
  @Id
  private String key;
  private String value;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Calendar expirationStart;
  @Column(nullable = false)
  private int expirationDuration;

  public JpaServerSessionKeyValue() {}

  public JpaServerSessionKeyValue(String key, String value, int expirationDuration) {
    this.key = key;
    this.value = value;
    this.expirationDuration = expirationDuration;
    this.expirationStart = Calendar.getInstance();
  }

  public String getValue() {
    return value;
  }

  public boolean isExpired() {
    long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
    long expirationTimeInMillis = expirationStart.getTimeInMillis() + expirationDuration;
    return currentTimeInMillis > expirationTimeInMillis;
  }

}
