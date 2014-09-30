package stormCounter;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import common.bolts.WordCountBolt;

public class WordCountBoltTest extends BasicBoltTest {

	@Test
	public final void testExecute_word_wordCount() {
		// Arrange
		final WordCountBolt bolt = Mockito.spy(new WordCountBolt());
		final Tuple firstMockedTuple = createMockedTuple("word", "christmas");
		final Tuple secondMockedTuple = createMockedTuple("word", "present");
		final Tuple thirdMockedTuple = createMockedTuple("word", "christmas");
		final BasicOutputCollector mockedOutputCollector = createMockedBasicOutputCollector();
		final InOrder inOrderChecker = Mockito.inOrder(mockedOutputCollector);

		// Act
		bolt.execute(firstMockedTuple, mockedOutputCollector);
		bolt.execute(secondMockedTuple, mockedOutputCollector);
		bolt.execute(thirdMockedTuple, mockedOutputCollector);

		// Assert
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("christmas", 1));
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("present", 1));
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("christmas", 2));
		inOrderChecker.verifyNoMoreInteractions();
	}
}
