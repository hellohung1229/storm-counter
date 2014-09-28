import org.junit.Test;
import org.mockito.Mockito;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;

import common.bolts.ConsolePrintBolt;

public class ConsolePrintBoltTest {
	
	@Test
	public void testExecute_tupleContent_tupleContentToConsole() {
		// Arrange
		final ConsolePrintBolt bolt =  Mockito.spy(new ConsolePrintBolt());
		final Tuple mockedTuple = createMockedTuple();
		final BasicOutputCollector mockedOutputCollector = createMockedOutputCollector();
		
		// Act
		bolt.execute(mockedTuple, mockedOutputCollector);
		
		// Assert
		Mockito.verify(bolt).print("tuple content");
	}
	
	private final Tuple createMockedTuple() {
		final Tuple mockedTuple = Mockito.mock(Tuple.class);
		Mockito.when(mockedTuple.toString()).thenReturn("tuple content");
		return mockedTuple;
	}

	private final BasicOutputCollector createMockedOutputCollector () {
		final BasicOutputCollector mockedOutputCollector = Mockito.mock(BasicOutputCollector.class);
		return mockedOutputCollector;
	}
}
