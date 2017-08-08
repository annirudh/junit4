package org.junit.tests.validation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class FailedConstructionTest {
    public static class CantConstruct {
        public CantConstruct() {
            throw new RuntimeException();
        }

        @Test
        public void foo() {
        }
    }

    @Test
    public void failedConstructionIsTestFailure() {
        Result result = JUnitCore.runClasses(CantConstruct.class);
        Failure failure = result.getFailures().get(0);

        /*
         * In the upstream JUnit, the constructor invocation happens within the context of a test
         * method, like 'foo()'. However, constructor invocation in this fork happens once per test
         * class outside of an individual test method, so the method prefix 'foo()' goes absent
         * in the failure's display name.
         */
        String expected = "org.junit.tests.validation.FailedConstructionTest$CantConstruct";
        Assert.assertEquals(expected, failure.getDescription().getDisplayName());
    }
}
