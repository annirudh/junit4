package org.junit.internal.runners.model;

import java.lang.reflect.InvocationTargetException;

/**
 * When invoked, throws the exception from the reflected method, rather than
 * wrapping it in an InvocationTargetException.
 */
public abstract class ReflectiveCallable {

    /**
     * Creates a new <code>ReflectiveCallable</code> which caches the instance retrieved during the first call to
     * <code>run()</code> and returns that value on subsequent calls to <code>run()</code>.
     *
     * @param callable The <code>ReflectiveCallable</code> whose <code>run()</code> result should be memoized.
     * @return A new, memoizing <code>ReflectiveCallable</code>
     */
    public static ReflectiveCallable memoize(final ReflectiveCallable callable) {
        return new ReflectiveCallable() {
            private Object result;

            @Override
            protected Object runReflectiveCall() throws Throwable {
                if (result == null) {
                    result = callable.run();
                }

                return result;
            }
        };
    }

    public Object run() throws Throwable {
        try {
            return runReflectiveCall();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    protected abstract Object runReflectiveCall() throws Throwable;
}