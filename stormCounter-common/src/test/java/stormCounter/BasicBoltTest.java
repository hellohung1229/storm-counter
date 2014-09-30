package stormCounter;
import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;

public abstract class BasicBoltTest {

	protected final Tuple createMockedTuple(final Map<String, Object> content) {
		final Tuple mockedTuple = Mockito.mock(Tuple.class);
		for (Map.Entry<String, Object> entry : content.entrySet()) {
			Mockito.when(mockedTuple.getValueByField(entry.getKey())).thenReturn((Object) entry.getValue());
		}
		return mockedTuple;
	}
	
	protected final Tuple createMockedTuple(final String key, final Object content) {
		final Map<String, Object> map = new HashMap<String, Object> ();
		map.put(key, content);
		return createMockedTuple(map);
	}

	protected final BasicOutputCollector createMockedBasicOutputCollector() {
		final BasicOutputCollector mockedOutputCollector = Mockito.mock(BasicOutputCollector.class);
		return mockedOutputCollector;
	}
}
