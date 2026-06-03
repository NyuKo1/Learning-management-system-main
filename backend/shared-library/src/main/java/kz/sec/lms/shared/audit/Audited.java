package kz.sec.lms.shared.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an endpoint as worthy of audit logging beyond the default write-only rule.
 * On a GET, sets the audit event's sensitive=true flag so it is logged even though
 * default rules skip reads.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Audited {
    boolean sensitive() default true;
}
