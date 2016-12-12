package se.vgregion.service.glasogonbidrag.local.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Internal method that exposes this applications Spring Application Context.
 * Some JSF Component may not be handled by the spring context, this means
 * that we can't autowire spring beans into these components.
 *
 * This will expose the application context so that we can access is from
 * these components.
 *
 * @author Martin Lind - Monator Technologies
 */
@Service
public class StaticApplicationContext {

    private static final Object contextLock = new Object();

    private static ApplicationContext _context;

    public static ApplicationContext getContext() {
        synchronized (contextLock) {
            if (_context == null) {
                throw new IllegalStateException(
                        "Can not get static context before it's initialized.");
            }

            return _context;
        }
    }

    @Autowired
    private ApplicationContext autowiredContext;

    public ApplicationContext getAutowiredContext() {
        return autowiredContext;
    }

    @PostConstruct
    protected void init() {
        synchronized (contextLock) {
            _context = autowiredContext;
        }
    }
}
