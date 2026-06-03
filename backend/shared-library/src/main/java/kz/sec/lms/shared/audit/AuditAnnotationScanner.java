package kz.sec.lms.shared.audit;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

/**
 * Walks every Spring bean at startup, finds methods annotated with @Audited,
 * and registers their full path (RequestMapping prefix + GetMapping path) with
 * the AuditFilter so the filter can flag sensitive GETs without recomputing
 * annotations per-request.
 */
@Component
public class AuditAnnotationScanner implements BeanPostProcessor {

    private final AuditFilter auditFilter;

    public AuditAnnotationScanner(AuditFilter auditFilter) {
        this.auditFilter = auditFilter;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        if (clazz.getName().contains("$$")) {
            clazz = clazz.getSuperclass();
        }
        RequestMapping classMapping = clazz.getAnnotation(RequestMapping.class);
        String prefix = (classMapping != null && classMapping.value().length > 0)
                ? classMapping.value()[0]
                : "";
        for (Method method : clazz.getDeclaredMethods()) {
            Audited audited = method.getAnnotation(Audited.class);
            GetMapping get = method.getAnnotation(GetMapping.class);
            if (audited != null && audited.sensitive() && get != null) {
                String[] paths = get.value().length > 0 ? get.value() : new String[]{""};
                for (String p : paths) {
                    auditFilter.registerSensitiveGet(prefix + p);
                }
            }
        }
        return bean;
    }
}
