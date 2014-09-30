package stormCounter;
import org.junit.Test;
import org.mockito.Mockito;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;

import common.bolts.ConsolePrintBolt;

public class ConsolePrintBoltTest extends BasicBoltTest {
	@Test
	public void testExecute_tupleContent_tupleContentToConsole() {
		// Arrange
		final ConsolePrintBolt bolt =  Mockito.spy(new ConsolePrintBolt());
		final Tuple mockedTuple = createMockedTuple("text", "tuple content");
		final BasicOutputCollector mockedOutputCollector = createMockedBasicOutputCollector();
		
		// Act
		bolt.execute(mockedTuple, mockedOutputCollector);
		
		// Assert
		Mockito.verify(bolt).print(mockedTuple.toString());
	}
}
