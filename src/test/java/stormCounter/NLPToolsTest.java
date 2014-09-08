package stormCounter;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import common.utils.NLPTools;

public class NLPToolsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDetectSentences() {
		assertEquals(new String[]{"First sentence.", "Second sentence."}, NLPTools.detectSentences("  First sentence. Second sentence.  "));
	}

}
