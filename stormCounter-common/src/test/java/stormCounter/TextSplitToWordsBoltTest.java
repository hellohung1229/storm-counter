package stormCounter;


import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import common.bolts.TextSplitToWordsBolt;

public class TextSplitToWordsBoltTest extends BasicBoltTest{
	
	@Test
	public final void testExecute_rawText_extractedWords() {
		// Arrange
		final TextSplitToWordsBolt bolt = Mockito.spy(new TextSplitToWordsBolt());
		final Tuple mockedTuple = createMockedTuple("text", "Tuple content to be splitted.");
		final BasicOutputCollector mockedOutputCollector = createMockedBasicOutputCollector();
		final InOrder inOrderChecker = Mockito.inOrder(mockedOutputCollector);
		
		// Act
		bolt.execute(mockedTuple, mockedOutputCollector);
		
		// Assert
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("Tuple"));
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("content"));
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("to"));
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("be"));
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("splitted"));
		inOrderChecker.verifyNoMoreInteractions();
	}
	
	@Test
	public final void testExecute_rawText_noExtractedWord() {
		// Arrange
		final TextSplitToWordsBolt bolt = Mockito.spy(new TextSplitToWordsBolt());
		final Tuple mockedTuple = createMockedTuple("text", "...");
		final BasicOutputCollector mockedOutputCollector = createMockedBasicOutputCollector();
		
		// Act
		bolt.execute(mockedTuple, mockedOutputCollector);
		
		// Assert
		Mockito.verifyNoMoreInteractions(mockedOutputCollector);
	}

}
