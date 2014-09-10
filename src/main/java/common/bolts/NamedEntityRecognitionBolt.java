package common.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class NamedEntityRecognitionBolt extends BaseBasicBolt {

	@Override
	public final void execute(final Tuple tuple, final BasicOutputCollector collector) {
		final String inputText = (String) tuple.getValueByField("text");
		
	}

	@Override
	public final void declareOutputFields(final OutputFieldsDeclarer ofd) {
		
	}

}
