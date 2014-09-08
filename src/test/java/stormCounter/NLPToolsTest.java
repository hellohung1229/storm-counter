package stormCounter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import common.utils.NLPUtil;

public class NLPToolsTest {

	@Test
	public void testDetectSentences() {
		assertEquals(new String[]{"First sentence.", "Second sentence."}, NLPUtil.detectSentences("  First sentence. Second sentence.  "));
	}

}
