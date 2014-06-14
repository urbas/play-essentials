package si.urbas.pless.util;

import java.lang.annotation.*;

/**
 * Defines the configuration key used to load the annotated service.
 * <p>
 * For example, if the class is annotated with {@code @PlessServiceConfigKey("pless.fooService")}
 * then you can configure your own service by adding the line {@code pless.fooService=com.your.package.YourService} into
 * the {@code application.conf} file.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface PlessServiceConfigKey {
  public String value();
}
