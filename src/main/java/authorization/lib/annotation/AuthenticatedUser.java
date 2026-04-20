package authorization.lib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)          // only valid on method parameters
@Retention(RetentionPolicy.RUNTIME)     // must be visible at runtime for Spring to detect it
public @interface AuthenticatedUser {
    
}
