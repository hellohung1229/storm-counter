package stormCounter;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import common.bolts.WordFilterBolt;

public class WordFilterBoltTest extends BasicBoltTest {

	@Test
	public void testExecute_word_filteredWord() {
		// Arrange
		final WordFilterBolt bolt = new WordFilterBolt();
		final Tuple firstMockedTuple = createMockedTuple("word", "foo");
		final Tuple secondMockedTuple = createMockedTuple("word", "potatos");
		final Tuple thirdMockedTuple = createMockedTuple("word", "bar");
		final BasicOutputCollector mockedOutputCollector = createMockedBasicOutputCollector();
		final InOrder inOrderChecker = Mockito.inOrder(mockedOutputCollector);

		// Act
		bolt.execute(firstMockedTuple, mockedOutputCollector);
		bolt.execute(secondMockedTuple, mockedOutputCollector);
		bolt.execute(thirdMockedTuple, mockedOutputCollector);

		// Assert
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("potatos"));
		inOrderChecker.verifyNoMoreInteractions();

	}
}
