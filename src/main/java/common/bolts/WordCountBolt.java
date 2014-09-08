package common.bolts;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public final class WordCountBolt extends BaseBasicBolt {
	private final Map<String, Integer> counts = new HashMap<>();

	@Override
	public void execute(final Tuple tuple, final BasicOutputCollector collector) {
		final String word = (String) tuple.getValueByField("word");
		Integer count = counts.get(word);
		if (count == null) {
			count = 0;
		}
		count++;
		counts.put(word, count);
		collector.emit(new Values(word, count));
	}

	@Override
	public void declareOutputFields(final OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));
	}

}
