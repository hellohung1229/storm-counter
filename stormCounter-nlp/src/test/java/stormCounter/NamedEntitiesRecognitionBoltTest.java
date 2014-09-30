package stormCounter;

import nlp.bolts.NamedEntitiesRecognitionBolt;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class NamedEntitiesRecognitionBoltTest extends BasicBoltTest{

	@Test
	public void testExecute_rawText_namedEntitis() {
		// Arrange
		final NamedEntitiesRecognitionBolt bolt = Mockito.spy(new NamedEntitiesRecognitionBolt());
		final Tuple mockedTuple = createMockedTuple("text", "Barack Obama is the USA president, and is married to Michele Obama.");
		final BasicOutputCollector mockedOutputCollector = createMockedBasicOutputCollector();
		final InOrder inOrderChecker = Mockito.inOrder(mockedOutputCollector);
		
		// Act
		bolt.execute(mockedTuple, mockedOutputCollector);
		
		// Assert
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("Barack Obama"));
		inOrderChecker.verify(mockedOutputCollector).emit(new Values("Michele Obama"));
		inOrderChecker.verifyNoMoreInteractions();
	}

}
