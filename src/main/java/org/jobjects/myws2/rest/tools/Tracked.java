package org.jobjects.myws2.rest.tools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.NameBinding;

/**
 * Annontation pour indiquer de suivre dans les logs le web service.
 * @author Mickaël Patron
 * @version 2016-05-08
 *
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Tracked {
}
