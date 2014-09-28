package stormCounter;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import common.bolts.WordCountBolt;

public class WordCountBoltTest {

	@Test
	public final void testExecute_word_wordCount() {
		// Arrange
		final WordCountBolt bolt = Mockito.spy(new WordCountBolt());
		final Tuple firstMockedTuple = createMockedTuple("christmas");
		final Tuple secondMockedTuple = createMockedTuple("present");
		final Tuple thirdMockedTuple = createMockedTuple("christmas");
		final BasicOutputCollector mockedOutputCollector = createMockedOutputCollector();
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
	
	private final Tuple createMockedTuple(final String content) {
		final Tuple mockedTuple = Mockito.mock(Tuple.class);
		Mockito.when(mockedTuple.getValueByField("word")).thenReturn(content);
		return mockedTuple;
	}

	private final BasicOutputCollector createMockedOutputCollector () {
		final BasicOutputCollector mockedOutputCollector = Mockito.mock(BasicOutputCollector.class);
		return mockedOutputCollector;
	}
}
