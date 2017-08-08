# Fork of JUnit 4 to relax requirements on static @AfterClass / @BeforeClass methods
A fork of JUnit 4.12 that relaxes the static requirement for methods annotated with @AfterClass / @BeforeClass. This is made possible
by changes to the core test driver execution in BlockJUnit4Runner that caches the first test object instance that it constructs and
reusing the same instance for each test.

Maintains feature parity with JUnit 4.12 with one notable exception:
* [Theories](https://github.com/junit-team/junit4/wiki/Theories) with parameterized constructors are unsupported, because the parameterized
constructors require reinvocation for every test.

NOTE: The fact that each test class is initialized once means that test methods that impact the state of the test class can impact
subsequent tests. Test writers seeking to use this fork must ensure that they reset any relevant state in between tests.
