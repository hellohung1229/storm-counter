package common.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public final class ConsolePrintBolt extends BaseBasicBolt {
	@Override
	public void execute(final Tuple tuple, final BasicOutputCollector collector) {
		System.out.println(tuple);
	}

	@Override
	public void declareOutputFields(final OutputFieldsDeclarer ofd) {

	}
}
