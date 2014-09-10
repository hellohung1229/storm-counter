package common.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public final class ConsolePrintBolt extends BaseBasicBolt {
	@Override
	public final void execute(final Tuple tuple, final BasicOutputCollector collector) {
		print(tuple.toString());
	}
	
	public final void print(String message) {
		System.out.println(message);
	}

	@Override
	public final void declareOutputFields(final OutputFieldsDeclarer ofd) {
	}
}
