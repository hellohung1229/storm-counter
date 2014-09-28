package stormCounter;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import common.bolts.WordFilterBolt;

public class WordFilterBoltTest {

	@Test
	public void testExecute_word_filteredWord() {
		// Arrange
		final WordFilterBolt bolt = new WordFilterBolt();
		final Tuple firstMockedTuple = createMockedTuple("foo");
		final Tuple secondMockedTuple = createMockedTuple("potatos");
		final Tuple thirdMockedTuple = createMockedTuple("bar");
		final BasicOutputCollector mockedOutputCollector=  createMockedOutputCollector();
		final InOrder inOrderChecker = Mockito.inOrder(mockedOutputCollector);
		
		// Act
		bolt.execute(firstMockedTuple, mockedOutputCollector);
		bolt.execute(secondMockedTuple, mockedOutputCollector);
		bolt.execute(thirdMockedTuple, mockedOutputCollector);
		
		// Assert
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("potatos"));
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
