package stormCounter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ConsolePrintBoltTest.class, NLPUtilTest.class, TextSplitToWordsBoltTest.class, WordCountBoltTest.class, WordFilterBoltTest.class })

public class AllTests {
}
