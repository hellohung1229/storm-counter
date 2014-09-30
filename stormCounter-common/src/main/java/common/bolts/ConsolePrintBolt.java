package common.bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class ConsolePrintBolt extends BaseBasicBolt {
	private static final long serialVersionUID = -5697531838448532476L;

	@Override
	public final void execute(final Tuple tuple, final BasicOutputCollector collector) {
		print(tuple.toString());
	}
	
	public void print(String message) {
		System.out.println(message);
	}

	@Override
	public final void declareOutputFields(final OutputFieldsDeclarer ofd) {
	}
}
