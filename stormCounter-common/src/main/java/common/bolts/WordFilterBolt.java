package common.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public final class WordFilterBolt extends BaseBasicBolt {

	@Override
	public void execute(final Tuple tuple, final BasicOutputCollector collector) {
		final String inputText = (String) tuple.getValueByField("word");
		if (inputText.matches(".*potato.*")) {
			collector.emit(new Values(inputText));
		}
	}

	@Override
	public void declareOutputFields(final OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("word"));
	}

}
