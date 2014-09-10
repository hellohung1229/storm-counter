package stormCounter;

import org.junit.Assert;
import org.junit.Test;

import common.utils.NLPUtil;

public class NLPUtilTest {

	@Test
	public final void testDetectSentencesFromRawText_rawText_extractedSentences() {
		// Arrange
		final String input = "  First sentence. Second sentence.  ";
		final String[] expectedResult = new String[] { "First sentence.", "Second sentence." };

		// Act
		final String[] actualResult = NLPUtil.detectSentencesFromRawText(input);

		// Assert
		Assert.assertArrayEquals(expectedResult, actualResult);
	}
	
	@Test
	public final void testDetectSentencesFromRawText_rawText_noSentence() {
		// Arrange
		final String input = "   ";
		final String[] expectedResult = new String[] {};

		// Act
		final String[] actualResult = NLPUtil.detectSentencesFromRawText(input);

		// Assert
		Assert.assertArrayEquals(expectedResult, actualResult);
	}

	@Test
	public final void testDetectTokensFromRawText_rawText_extractedTokens() {
		// Arrange
		final String input = "  This is a sentence to tokenize, in order to test the NLPUtil class.  ";
		final String[] expectedResult = new String[] { "This", "is", "a", "sentence", "to", "tokenize", ",", "in", "order", "to", "test", "the", "NLPUtil", "class", "." };

		// Act
		final String[] actualResult = NLPUtil.detectTokensFromRawText(input);

		// Assert
		Assert.assertArrayEquals(expectedResult, actualResult);
	}
	
	@Test
	public final void testDetectTokensFromRawText_rawText_noToken() {
		// Arrange
		final String input = "     ";
		final String[] expectedResult = new String[] {};

		// Act
		final String[] actualResult = NLPUtil.detectTokensFromRawText(input);

		// Assert
		Assert.assertArrayEquals(expectedResult, actualResult);
	}

	@Test
	public final void testDetectNamedEntitiesFromTokens_tokens_extractedNamedEntities() {
		// Arrange
		final String[] input = new String[] { "The", "United", "States", "are", "lead", "by", "Barack", "Obama", ",", "who", "is", "married", "to", "Michele", "Obama", "." };
		final String[] expectedResult = new String[] { "Barack Obama", "Michele Obama" };

		// Act
		final String[] actualResult = NLPUtil.detectNamedEntitiesFromTokens(NLPUtil.namedEntities.PERSONS, input);

		// Assert
		Assert.assertArrayEquals(expectedResult, actualResult);

	}

	@Test
	public final void testDetectNamedEntitiesFromTokens_tokens_noNamedEntity() {
		// Arrange
		final String[] input = new String[] { "There", "is", "nothing", "to", "detect", "in", "this", "sentence", "." };
		final String[] expectedResult = new String[] {};

		// Act
		final String[] actualResult = NLPUtil.detectNamedEntitiesFromTokens(NLPUtil.namedEntities.PERSONS, input);

		// Assert
		Assert.assertArrayEquals(expectedResult, actualResult);

	}

}
