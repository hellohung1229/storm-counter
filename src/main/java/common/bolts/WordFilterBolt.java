package common.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class WordFilterBolt extends BaseBasicBolt {

	@Override
	public void execute(final Tuple tuple, final BasicOutputCollector collector) {
		final String inputText = (String) tuple.getValueByField("word");
		if (inputText.matches(".*Paris.*")) {
			collector.emit(tuple.getValues());
		}
	}

	@Override
	public void declareOutputFields(final OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("word"));
	}

}
