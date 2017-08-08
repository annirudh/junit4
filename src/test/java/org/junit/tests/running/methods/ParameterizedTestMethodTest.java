package org.junit.tests.running.methods;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.model.InitializationError;

@RunWith(Parameterized.class)
public class ParameterizedTestMethodTest {

    @SuppressWarnings("all")
    public static class EverythingWrong {
        private EverythingWrong() {
        }

        @BeforeClass
        static void notPublicBC() {
        }

        @BeforeClass
        public static int nonVoidBC() {
            return 0;
        }

        @BeforeClass
        public static void argumentsBC(int i) {
        }

        @BeforeClass
        public static void fineBC() {
        }

        @AfterClass
        static void notPublicAC() {
        }

        @AfterClass
        public static int nonVoidAC() {
            return 0;
        }

        @AfterClass
        public static void argumentsAC(int i) {
        }

        @AfterClass
        public static void fineAC() {
        }

        @After
        public static void staticA() {
        }

        @After
        void notPublicA() {
        }

        @After
        public int nonVoidA() {
            return 0;
        }

        @After
        public void argumentsA(int i) {
        }

        @After
        public void fineA() {
        }

        @Before
        public static void staticB() {
        }

        @Before
        void notPublicB() {
        }

        @Before
        public int nonVoidB() {
            return 0;
        }

        @Before
        public void argumentsB(int i) {
        }

        @Before
        public void fineB() {
        }

        @Test
        public static void staticT() {
        }

        @Test
        void notPublicT() {
        }

        @Test
        public int nonVoidT() {
            return 0;
        }

        @Test
        public void argumentsT(int i) {
        }

        @Test
        public void fineT() {
        }
    }

    private Class<?> fClass;
    private int fErrorCount;

    static public class SuperWrong {
        @Test
        void notPublic() {
        }
    }

    static public class SubWrong extends SuperWrong {
        @Test
        public void justFine() {
        }
    }

    static public class SubShadows extends SuperWrong {
        @Override
        @Test
        public void notPublic() {
        }
    }

    public ParameterizedTestMethodTest(Class<?> class1, int errorCount) {
        fClass = class1;
        fErrorCount = errorCount;
    }

    @Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][]{
                /*
                 * The math below is as follows:
                 *
                 * @After / @Before / @Test have four bad methods each (static, non-public, non-void, takes arguments) -> 4 * 3.
                 * @AfterClass / @BeforeClass have three bad methods each (non-public, non-void, takes arguments) -> 2 * 3.
                 * The constructor is not public -> 1
                 */
                {EverythingWrong.class, (4 * 3) + (2 * 3) + 1},
                {SubWrong.class, 1},
                {SubShadows.class, 0}});
    }

    private List<Throwable> validateAllMethods(Class<?> clazz) {
        try {
            new BlockJUnit4ClassRunner(clazz);
        } catch (InitializationError e) {
            return e.getCauses();
        }
        return Collections.emptyList();
    }

    @Test
    public void testFailures() throws Exception {
        List<Throwable> problems = validateAllMethods(fClass);
        assertEquals(fErrorCount, problems.size());
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ParameterizedTestMethodTest.class);
    }
}
